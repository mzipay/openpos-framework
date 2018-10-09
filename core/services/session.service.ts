import { Logger } from './logger.service';

import { Configuration } from './../../configuration/configuration';
import { IMessageHandler } from './../interfaces/message-handler.interface';
import { PersonalizationService, DEFAULT_LOCALE } from './personalization.service';

import { Observable, Subscription, BehaviorSubject, Subject, merge } from 'rxjs';
import { map, filter } from 'rxjs/operators';
import { Message } from '@stomp/stompjs';
import { Injectable, NgZone, forwardRef, Inject } from '@angular/core';
import { StompState, StompRService } from '@stomp/ng2-stompjs';
import { MatDialog } from '@angular/material';
import { ActionIntercepter } from '../action-intercepter';
// Importing the ../components barrel causes a circular reference since dynamic-screen references back to here,
// so we will import those files directly
import { LoaderState } from '../components/loader/loader-state';
import { ConfirmationDialogComponent } from '../components/confirmation-dialog/confirmation-dialog.component';
import { IDeviceResponse, InAppBrowserPlugin } from '../plugins';
import {
    IMenuItem,
    IUrlMenuItem
} from '../interfaces';
import { IConfirmationDialog } from '../interfaces/confirmation-dialog.interface';
import { PluginService } from './plugin.service';
import { AppInjector } from '../app-injector';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { PingParams } from '../interfaces/ping-params.interface';
import { PingResult } from '../interfaces/ping-result.interface';

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

    private subscription: Subscription;

    private authToken: string;

    private messages: Observable<Message>;

    private screenSource = new BehaviorSubject<any>(null);

    private stompDebug = false;

    private actionPayloads: Map<string, Function> = new Map<string, Function>();

    private actionIntercepters: Map<string, ActionIntercepter> = new Map();

    loaderState: LoaderState;
    private stompStateSubscription: Subscription;

    public onServerConnect: BehaviorSubject<boolean>;

    private stompJsonMessages$ = new Subject<any>();
    private sessionMessages$ = new Subject<any>();
    public inBackground = false;

    constructor(
        private log: Logger,
        private stompService: StompRService,
        public dialogService: MatDialog,
        public zone: NgZone,
        protected personalization: PersonalizationService,
        private http: HttpClient
    ) {

        this.loaderState = new LoaderState(this, personalization);
        this.zone.onError.subscribe((e) => {
            console.error(`[OpenPOS]${e}`);
        });
        this.onServerConnect = new BehaviorSubject<boolean>(false);
        this.getMessages().pipe(
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
            this.sessionMessages$ ).pipe(filter(s => types && types.length > 0 ? types.includes(s.type) : true));
    }

    public registerMessageHandler(handler: IMessageHandler, ...types: string[]): Subscription {
        return this.stompJsonMessages$.pipe(filter(s => types && types.length > 0 ? types.includes(s.type) : true)).subscribe(s => handler.handle(s));
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
        this.log.info('creating new stomp service at: ' + url);
        this.stompService.config = {
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
        };
        this.stompService.initAndConnect();

        this.appId = appId;
        const currentTopic = this.buildTopicName();

        this.log.info('subscribing to server at: ' + currentTopic);

        this.messages = this.stompService.subscribe(currentTopic);

        // Subscribe a function to be run on_next message
        this.subscription = this.messages.subscribe((message: Message) => {
            this.log.info('Got STOMP message');
            if ( this.inBackground ) {
                this.log.info('Leaving background');
                this.inBackground = false;
            }
            if (this.isMessageVersionValid(message)) {
                const json = JSON.parse(message.body);
                this.logStompJson(json);
                this.stompJsonMessages$.next(json);
            } else {
                this.log.info(`Showing incompatible version screen`);
                this.stompJsonMessages$.next(this.buildIncompatibleVersionScreen());
            }
        });

        this.state = this.stompService.state.pipe(map((state: number) => StompState[state]));

        this.subscribed = true;
        this.loaderState.monitorConnection();


        if (! this.stompStateSubscription) {
            this.stompStateSubscription = this.state.subscribe(stompState => {
                if (stompState === 'CONNECTED') {
                    if (! this.onServerConnect.value) {
                        this.onServerConnect.next(true);
                    }
                } else if (stompState === 'DISCONNECTING') {
                    this.log.info('STOMP disconnecting');
                }
            });
        }
    }

    private logStompJson(json: any) {
        if (json) {
          this.log.info(`[logStompJson] type: ${json.type}, screenType: ${json.screenType}, seqNo: ${json.sequenceNumber}`);
        } else {
          this.log.info(`[logStompJson] ${json}`);
        }
    }

    private buildIncompatibleVersionScreen(): any {

        return {
            type: 'Screen',
            screenType: 'Dialog',
            template: {dialog: true, type: 'BlankWithBar'},
            dialogProperties: {closeable: false},
            title: 'Incompatible Versions',
            message: Configuration.incompatibleVersionMessage.split('\n')
        };
    }

    private isMessageVersionValid(message: Message): boolean {
        const valid = message.headers.compatibilityVersion === Configuration.compatibilityVersion;
        if (! valid) {
            this.log.info(`INCOMPATIBLE VERSIONS. Client compatibilityVersion: ${Configuration.compatibilityVersion}, ` +
            `server compatibilityVersion: ${message.headers.compatibilityVersion}`);
        }
        return valid;
    }

    public async ping(pingParams?: PingParams): Promise<PingResult> {
        let url = '';
        if (pingParams) {
            let protocol = 'http://';
            if (pingParams.useSsl) {
                protocol = 'https://';
            }
            url = protocol + pingParams.serverName;
            if (pingParams.serverPort) {
                url = url + ':' + pingParams.serverPort;
            }
            url = url + '/ping';

        } else {
            url = `${this.personalization.getServerBaseURL()}/ping`;
        }

        this.log.info('testing url: ' + url);

        let pingError: any = null;
        try {
            const httpResult = await this.http.get(url, {}).toPromise();
            if (httpResult) {
                this.log.info('successful validation of ' + url);
                return {success: true};
            } else {
                pingError = {message: '?'};
            }
        } catch (error) {
            pingError = error;
        }

        if (pingError) {
            this.log.info('bad validation of ' + url + ' with an error message of :' + pingError.message);
            return { success: false, message: pingError.message };
        }
    }

    public unsubscribe() {
        if (!this.subscribed) {
            return;
        }

        this.log.info('unsubscribing from stomp service ...');

        // This will internally unsubscribe from Stomp Broker
        // There are two subscriptions - one created explicitly, the other created in the template by use of 'async'
        this.subscription.unsubscribe();
        this.subscription = null;

        this.log.info('disconnecting from stomp service');
        this.stompService.disconnect();
        this.messages = null;

        this.subscribed = false;
    }

    public onDeviceResponse(deviceResponse: IDeviceResponse) {
        const sendResponseBackToServer: Function = () => {
            // tslint:disable-next-line:max-line-length
            this.log.info(`>>> Publish deviceResponse requestId: "${deviceResponse.requestId}" deviceId: ${deviceResponse.deviceId} type: ${deviceResponse.type}`);
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
                    this.log.info(`About to open: ${urlMenuItem.url} in target mode: ${urlMenuItem.targetMode}, with options: ${urlMenuItem.options}`);
                    const pluginService = AppInjector.Instance.get(PluginService);
                    // Use inAppBrowserPlugin when available since it tracks whether or not the browser is active.
                    pluginService.getPlugin('InAppBrowser').then(plugin => {
                        const inAppPlugin = <InAppBrowserPlugin> plugin;
                        inAppPlugin.open(urlMenuItem.url, urlMenuItem.targetMode, urlMenuItem.options);
                    }).catch(error => {
                        this.log.info(`InAppBrowser not found, using window.open. Reason: ${error}`);
                        window.open(urlMenuItem.url, urlMenuItem.targetMode, urlMenuItem.options);
                    });
                    if (!actionString || 0 === actionString.length) {
                        return;
                    }
                }
            } else {
                actionString = <string>action;
            }

            this.log.info(`action is: ${actionString}`);

            if (confirm) {
                this.log.info('Confirming action');
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
                    this.log.info('Canceling action');
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
                this.log.info(`Checking registered action payload for ${actionString}`);
                try {
                    this.response = this.actionPayloads.get(actionString)();
                } catch (e) {
                    this.log.info(`invalid action payload for ${actionString}: ` + e);
                    processAction = false;
                }
            }

            if (processAction && !this.loaderState.loading) {
                const sendToServer: Function = () => {
                    this.log.info(`>>> Post action "${actionString}"`);
                    this.queueLoading();
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
                }
            } else {
                this.log.info(`Not sending action: ${actionString}.  processAction: ${processAction}, loading:${this.loaderState.loading}`);
            }

        } else {
            this.log.info(`received an invalid action: ${action}`);
        }
    }

    public keepAlive() {
        if (this.subscribed) {
            this.log.info(`>>> KeepAlive`);
            this.publish('KeepAlive', 'KeepAlive');
        }
    }

    public publish(actionString: string, type: string, payload?: any) {
        // Block any actions if we are backgrounded and running in cordova
        // (unless we are coming back out of the background)
        if (this.inBackground && actionString !== 'Refresh') {
            return;
        }
        const nodeId = this.personalization.getNodeId();
        if (this.appId && nodeId) {
            this.stompService.publish('/app/action/app/' + this.appId + '/node/' + this.personalization.getNodeId(),
                JSON.stringify({ name: actionString, type: type, data: payload ? payload : this.response }));
            if (actionString !== 'KeepAlive') {
                this.response = null;
            }
        } else {
            this.log.info(`Can't publish action '${actionString}' of type '${type}' ` +
                `due to undefined App ID (${this.appId}) or Node Id (${nodeId})`);
        }
    }

    private queueLoading() {
        this.loaderState.loading = true;
        setTimeout(() => {
            this.log.info(`queueLoading timeout fired, invoking showLoading`);
            this.showLoading(LoaderState.LOADING_TITLE);
        }, 1000);

    }

    private showLoading(title: string, message?: string) {
        this.log.info(`showLoading method invoked`);
        if (this.loaderState.loading) {
            this.log.info(`showLoading is showing the loading dialog NOW`);
            this.loaderState.setVisible(true, title, message);
        }
    }

    public cancelLoading() {
        this.log.info(`cancelLoading invoked`);
        this.loaderState.loading = false;
        this.loaderState.setVisible(false);
    }

    handle(message: any) {
        this.log.info(`Got message: ${message.screenType}`);
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

