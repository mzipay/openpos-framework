import { Configuration } from './../../configuration/configuration';
import { IMessageHandler } from './../interfaces/message-handler.interface';
import { PersonalizationService, DEFAULT_LOCALE } from './personalization.service';

import { Observable, Subscription, BehaviorSubject, Subject, merge } from 'rxjs';
import { map, filter } from 'rxjs/operators';
import { Message } from '@stomp/stompjs';
import { Injectable, EventEmitter, NgZone } from '@angular/core';
import { StompService, StompState } from '@stomp/ng2-stompjs';
import { Location } from '@angular/common';
import { MatDialog } from '@angular/material';
import { Router } from '@angular/router';
import { ActionIntercepter } from '../action-intercepter';
import { IThemeChangingEvent } from '../../shared/';
// Importing the ../components barrel causes a circular reference since dynamic-screen references back to here,
// so we will import those files directly
import { LoaderState } from '../components/loader/loader-state';
import { ConfirmationDialogComponent } from '../components/confirmation-dialog/confirmation-dialog.component';
import { IDeviceResponse, IDeviceRequest } from '../plugins';
import {
    IMenuItem,
    IUrlMenuItem,
    IToastScreen,
    ToastType,
    Element,
    ActionMap
} from '../interfaces';
import { IConfirmationDialog } from '../interfaces/confirmation-dialog.interface';

// export const DEFAULT_LOCALE = 'en-US';
@Injectable({
    providedIn: 'root',
})
export class SessionService implements IMessageHandler {

    private screen: any;

    public state: Observable<string>;

    public response: any;

    private appId: String;

    private subscribed: boolean;

    private subscription: any;

    private authToken: string;

    private messages: Observable<Message>;

    private screenSource = new BehaviorSubject<any>(null);

    private stompService: StompService;

    private stompDebug = false;

    private actionPayloads: Map<string, Function> = new Map<string, Function>();

    private actionIntercepters: Map<string, ActionIntercepter> = new Map();

    loaderState: LoaderState;

    public onServerConnect: BehaviorSubject<boolean>;

    private stompJsonMessages$ = new Subject<any>();
    private sessionMessages$ = new Subject<any>();

    constructor(private location: Location, private router: Router, public dialogService: MatDialog,
        public zone: NgZone, protected personalization: PersonalizationService) {

        this.loaderState = new LoaderState(this, personalization);
        this.zone.onError.subscribe((e) => {
            console.error(`[OpenPOS]${e}`);
        });
        this.onServerConnect = new BehaviorSubject<boolean>(false);
        this.stompJsonMessages$.pipe(
                    filter(s => ['Screen'].includes(s.type))
                )
            .subscribe(s => this.handle(s));
    }

    public sendMessage(message: any) {
        this.sessionMessages$.next(message);
    }

    public getMessages( ...types: string[]): Observable<any> {
        return  merge(
            this.stompJsonMessages$,
            this.sessionMessages$ ).pipe(filter(s => types.includes(s.type)));
    }

    public registerMessageHandler(handler: IMessageHandler, ...types: string[]): Subscription {
        return this.stompJsonMessages$.pipe(filter(s => types.includes(s.type))).subscribe(s => handler.handle(s));
    }

    public subscribeForScreenUpdates(callback: (screen: any) => any): Subscription {
        return this.screenSource.asObservable().subscribe(
            screen => this.zone.run(() => callback(screen))
        );
    }

    public isRunningInBrowser(): boolean {
        const app = document.URL.indexOf('http://') === -1 && document.URL.indexOf('https://') === -1;
        return !app;
    }

    private buildTopicName(): string {
        return '/topic/app/' + this.appId + '/node/' + this.personalization.getNodeId();
    }

    public showScreen(screen: any) {
        this.screen = screen;
        if (screen && screen.theme) {
            this.personalization.setTheme(screen.theme, false);
        }
        this.screenSource.next(screen);
    }

    public setAuthToken(token: string) {
        this.authToken = token;
    }

    public getAppId(): String {
        return this.appId;
    }

    public connected(): boolean {
        return this.stompService && this.stompService.connected();
    }

    public subscribe(appId: String) {
        if (this.subscribed) {
            return;
        }

        const url = this.personalization.getWebsocketUrl();
        console.log('creating new stomp service at: ' + url);
        this.stompService = new StompService({
            url: url,
            headers: {
                authToken: this.authToken,
                compatibilityVersion: Configuration.compatibilityVersion,
                appId: appId,
                nodeId: this.personalization.getNodeId()
            },
            heartbeat_in: 0, // Typical value 0 - disabled
            heartbeat_out: 20000, // Typical value 20000 - every 20 seconds
            reconnect_delay: 250,  // Typical value is 5000, 0 disables.
            debug: this.stompDebug
        });

        this.appId = appId;
        const currentTopic = this.buildTopicName();

        console.log('subscribing to server at: ' + currentTopic);

        this.messages = this.stompService.subscribe(currentTopic);

        // Subscribe a function to be run on_next message
        this.subscription = this.messages.subscribe((message: Message) => {
            const json = JSON.parse(message.body);
            this.stompJsonMessages$.next(json);
        });

        this.state = this.stompService.state.pipe(map((state: number) => StompState[state]));

        this.subscribed = true;
        this.loaderState.monitorConnection();
        this.state.subscribe(stompState => {
            if (stompState === 'CONNECTED') {
                if (! this.onServerConnect.value) {
                    this.onServerConnect.next(true);
                }
            }
        });
    }

    public unsubscribe() {
        if (!this.subscribed) {
            return;
        }

        console.log('unsubscribing from stomp service ...');

        // This will internally unsubscribe from Stomp Broker
        // There are two subscriptions - one created explicitly, the other created in the template by use of 'async'
        this.subscription.unsubscribe();
        this.subscription = null;

        console.log('disconnecting from stomp service');
        this.stompService.disconnect();
        this.stompService = null;
        this.messages = null;

        this.subscribed = false;
    }

    public onDeviceResponse(deviceResponse: IDeviceResponse) {
        const sendResponseBackToServer: Function = () => {
            // tslint:disable-next-line:max-line-length
            console.log(`>>> Publish deviceResponse requestId: "${deviceResponse.requestId}" deviceId: ${deviceResponse.deviceId} type: ${deviceResponse.type}`);
            this.stompService.publish(`/app/device/app/${this.appId}/node/${this.personalization.getNodeId()}/device/${deviceResponse.deviceId}`,
                JSON.stringify(deviceResponse));
        };

        // see if we have any intercepters registered for the type of this deviceResponse
        // otherwise just send the response
        if (this.actionIntercepters.has(deviceResponse.type)) {
            this.actionIntercepters.get(deviceResponse.type).intercept(deviceResponse, sendResponseBackToServer);
        } else {
            sendResponseBackToServer();
        }
    }

    public async onValueChange(action: string, payload?: any) {
        this.onAction(action, payload, null, true);
    }

    public async onAction(action: string | IMenuItem, payload?: any, confirm?: string | IConfirmationDialog, isValueChangedAction?: boolean) {
        if (action) {
            let actionString = '';
            // we need to figure out if we are a menuItem or just a string
            if (action.hasOwnProperty('action')) {
                const menuItem = <IMenuItem>(action);
                confirm = menuItem.confirmationDialog;
                actionString = menuItem.action;
                // check to see if we are an IURLMenuItem
                if (menuItem.hasOwnProperty('url')) {
                    const urlMenuItem = <IUrlMenuItem>menuItem;
                    console.log(`About to open: ${urlMenuItem.url} in target mode: ${urlMenuItem.targetMode}, with options: ${urlMenuItem.options}`);
                    window.open(urlMenuItem.url, urlMenuItem.targetMode, urlMenuItem.options);
                    if (!actionString || 0 === actionString.length) {
                        return;
                    }
                }
            } else {
                actionString = <string>action;
            }

            console.log(`action is: ${actionString}`);

            if (confirm) {
                console.log('Confirming action');
                let confirmD: IConfirmationDialog;
                if (confirm.hasOwnProperty('message')) {
                    confirmD = <IConfirmationDialog>confirm;
                } else {
                    confirmD = { title: '', message: <string>confirm, cancelButtonName: 'No', confirmButtonName: 'Yes' };
                }
                const dialogRef = this.dialogService.open(ConfirmationDialogComponent, { disableClose: true });
                dialogRef.componentInstance.confirmDialog = confirmD;
                const result = await dialogRef.afterClosed().toPromise();

                // if we didn't confirm return and don't send the action to the server
                if (!result) {
                    console.log('Canceling action');
                    return;
                }
            }

            let processAction = true;

            // First we will use the payload passed into this function then
            // Check if we have registered action payload
            // Otherwise we will send whatever is in this.response
            if (payload != null) {
                this.response = payload;
            } else if (this.actionPayloads.has(actionString)) {
                console.log(`Checking registered action payload for ${actionString}`);
                try {
                    this.response = this.actionPayloads.get(actionString)();
                } catch (e) {
                    console.log(`invalid action payload for ${actionString}: ` + e);
                    processAction = false;
                }
            }

            if (processAction && !this.loaderState.loading) {
                const sendToServer: Function = () => {
                    console.log(`>>> Post action "${actionString}"`);
                    this.publish(actionString, 'Screen');
                };

                // see if we have any intercepters registered
                // otherwise just send the action
                if (this.actionIntercepters.has(actionString)) {
                    this.actionIntercepters.get(actionString).intercept(this.response, sendToServer);
                } else {
                    sendToServer();
                    if (!isValueChangedAction) {
                        // not sure if this is the best way to do this, but its how we are doing it for now
                        this.sessionMessages$.next({ clearDialog: true});
                    }
                    this.queueLoading();
                }
            } else {
                console.log(`Not sending action: ${actionString}.  processAction: ${processAction}, loading:${this.loaderState.loading}`);
            }

        } else {
            console.log(`received an invalid action: ${action}`);
        }
    }

    public keepAlive() {
        if (this.subscribed) {
            console.log(`>>> KeepAlive`);
            this.publish('KeepAlive', 'KeepAlive');
        }
    }

    public publish(actionString: string, type: string, payload?: any) {
        const nodeId = this.personalization.getNodeId();
        if (this.appId && nodeId) {
            this.stompService.publish('/app/action/app/' + this.appId + '/node/' + this.personalization.getNodeId(),
                JSON.stringify({ name: actionString, type: type, data: payload ? payload : this.response }));
        } else {
            console.log(`Can't publish action '${actionString}' of type '${type}' ` +
                `due to undefined App ID (${this.appId}) or Node Id (${nodeId})`);
        }
    }

    private queueLoading() {
        // console.log(`queueLoading invoked`);
        this.loaderState.loading = true;
        setTimeout(() => this.showLoading(LoaderState.LOADING_TITLE), 1000);
    }

    private showLoading(title: string, message?: string) {
        // console.log(`showLoading invoked`);
        if (this.loaderState.loading) {
            this.loaderState.setVisible(true, title, message);
        }
    }

    public cancelLoading() {
        // console.log(`cancelLoading invoked`);
        this.loaderState.loading = false;
        this.loaderState.setVisible(false);
    }

    handle(message: any) {
        if (message.screenType === 'Loading') {
            // This is just a temporary hack
            // Might be a previous instance of a Loading screen being shown,
            // so dismiss it first. This occurs, for example, when mobile device is put
            // to sleep while showing a loading dialog. Need to cancel it so that loader state
            // gets reset.
            if (this.loaderState.loading) {
                this.cancelLoading();
            }
            this.loaderState.loading = true;
            this.showLoading(message.title, message.message);
            return;
        } else if (message.screenType === 'NoOp') {
            this.response = null;
            return; // As with DeviceRequest, return to avoid dismissing loading screen
        } else if (message.screenType === 'Toast') {

        } else if ( message.template && !message.template.dialog ) {
            this.response = null;
            this.showScreen(message);
        }
        this.cancelLoading();
    }

    public registerActionPayload(actionName: string, actionValue: Function) {
        this.actionPayloads.set(actionName, actionValue);
    }

    public unregisterActionPayloads() {
        this.actionPayloads.clear();
    }

    public unregisterActionPayload(actionName: string) {
        this.actionPayloads.delete(actionName);
    }

    public registerActionIntercepter(actionName: string, actionIntercepter: ActionIntercepter) {
        this.actionIntercepters.set(actionName, actionIntercepter);
    }

    public unregisterActionIntercepters() {
        this.actionIntercepters.clear();
    }

    public unregisterActionIntercepter(actionName: string) {
        this.actionIntercepters.delete(actionName);
    }

    public getCurrencyDenomination(): string {
        return 'USD';
    }

    public getApiServerBaseURL(): string {
        return `${this.personalization.getServerBaseURL()}/api`;
    }

    getLocale(): string {
        let returnLocale: string;
        if (this.screen) {
            returnLocale = this.screen.locale;
        }
        return returnLocale || DEFAULT_LOCALE;
    }

}


