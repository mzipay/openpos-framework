import { LoaderState } from './../common/loader/loader-state';
import { IDeviceResponse } from './../common/ideviceresponse';
import { IDeviceRequest } from './../common/idevicerequest';
import { IMenuItem } from '../common/imenuitem';
import { Observable } from 'rxjs/Observable';
import { Message } from '@stomp/stompjs';
import { Subscription } from 'rxjs/Subscription';
import { Injectable, EventEmitter, NgZone } from '@angular/core';
import { StompService, StompState } from '@stomp/ng2-stompjs';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { ActionIntercepter, ActionIntercepterBehavior, ActionIntercepterBehaviorType } from '../common/action-intercepter';
import { ToastType, IToastScreen } from '../common/toast-screen.interface';
import { MatDialog, MatSnackBar } from '@angular/material';
import { ConfirmationDialogComponent } from '../common/confirmation-dialog/confirmation-dialog.component';
import { IUrlMenuItem } from '../common/iurlmenuitem';
import { DEFAULT_LOCALE, ILocaleService } from './locale.service';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Element, ActionMap } from '../screens/dynamic-screen/dynamic-screen.component';
import { IThemeChangingEvent } from '../events/ithemechanging.event';

@Injectable()
export class SessionService implements ILocaleService {

    subNode$ = new BehaviorSubject<any[]>([]);

    obsNode$: Observable<Element[]>;

    subConv$ = new BehaviorSubject<any[]>([]);

    obsConv$: Observable<Element[]>;

    subSess$ = new BehaviorSubject<any[]>([]);

    obsSess$: Observable<Element[]>;

    subConf$ = new BehaviorSubject<any[]>([]);

    obsConf$: Observable<Element[]>;

    subFlow$ = new BehaviorSubject<any[]>([]);

    obsFlow$: Observable<Element[]>;

    subSave$ = new BehaviorSubject<string[]>([]);

    obsSave$: Observable<string[]>;

    NodeElements: Element[] = [];
    SessElements: Element[] = [];
    ConvElements: Element[] = [];
    ConfElements: Element[] = [];
    FlowElements: Element[] = [];
    savePoints: string[] = [];


    currentState: string;

    subState$ = new BehaviorSubject<string>("");

    obsState$: Observable<string>;

    currentStateClass: string;

    subStateClass$ = new BehaviorSubject<string>("");

    obsStateClass$: Observable<string>;

    currentStateActions: ActionMap[] = [];

    subStateActions$ = new BehaviorSubject<any[]>([]);

    obsStateActions$: Observable<ActionMap[]>;

    private screen: any;

    public dialog: any;

    public state: Observable<string>;

    public response: any;

    private appId: String;

    private deviceName: string;

    private subscribed: boolean;

    private subscription: any;

    private authToken: string;

    private messages: Observable<Message>;

    public onDeviceRequest = new EventEmitter<IDeviceRequest>();
    public onThemeChanging = new EventEmitter<IThemeChangingEvent>();
    private previousTheme: string;

    private screenSource = new BehaviorSubject<any>(null);

    private dialogSource = new BehaviorSubject<any>(null);

    private loading: boolean;

    private stompService: StompService;

    private stompDebug = false;

    private actionPayloads: Map<string, Function> = new Map<string, Function>();

    private actionIntercepters: Map<string, ActionIntercepter> = new Map();

    private serverBaseUrl: string;

    loaderState: LoaderState;

    public onServerConnect: Observable<boolean>;
    private onServerConnectObserver: any;

    constructor(private location: Location, private router: Router, public dialogService: MatDialog, public snackBar: MatSnackBar,
        public zone: NgZone) {

        this.loaderState = new LoaderState(this);
        this.zone.onError.subscribe((e) => {
            console.error(`[OpenPOS]${e}`);
        });
        this.onServerConnect = Observable.create(observer => {this.onServerConnectObserver = observer;});
        this.obsNode$ = this.subNode$.asObservable();
        this.obsSess$ = this.subSess$.asObservable();
        this.obsConv$ = this.subConv$.asObservable();
        this.obsConf$ = this.subConf$.asObservable();
        this.obsFlow$ = this.subFlow$.asObservable();
        this.obsState$ = this.subState$.asObservable();
        this.obsStateClass$ = this.subStateClass$.asObservable();
        this.obsStateActions$ = this.subStateActions$.asObservable();
        this.obsSave$ = this.subSave$.asObservable();
    }

    public subscribeForScreenUpdates(callback: (screen: any) => any): Subscription {
        return this.screenSource.asObservable().subscribe(
            screen => this.zone.run(() => callback(screen))
        );
    }

    public subscribeForDialogUpdates(callback: (dialog: any) => any): Subscription {
        return this.dialogSource.asObservable().subscribe(
            dialog => this.zone.run(() => callback(dialog))
        );
    }

    public isRunningInBrowser(): boolean {
        const app = document.URL.indexOf('http://') === -1 && document.URL.indexOf('https://') === -1;
        return !app;
    }

    public personalize(serverName: string, serverPort: string, node: string | {storeId: string, deviceId: string}, 
        sslEnabled?: boolean, refreshApp: boolean = true) {

        let nodeId = '';
        if (typeof node === 'string') {
            nodeId = node;
        } else {
            nodeId = node.storeId + '-' + node.deviceId;
        }
        console.log(`personalizing with server: ${serverName}, port: ${serverPort}, nodeid: ${nodeId}`);
        localStorage.setItem('serverName', serverName);
        localStorage.setItem('serverPort', serverPort);
        localStorage.setItem('nodeId', nodeId);
        if (sslEnabled) {
            localStorage.setItem('sslEnabled', '' + sslEnabled);
        } else {
            localStorage.setItem('sslEnabled', 'false');
        }
        this.serverBaseUrl = null; // will be regenerated on next fetch
        if (refreshApp) {
            this.refreshApp();
        }

    }

    public dePersonalize() {
        this.unsubscribe();
        const theme = this.getTheme();
        localStorage.removeItem('serverName');
        localStorage.removeItem('serverPort');
        localStorage.removeItem('nodeId');
        localStorage.removeItem('theme');
        localStorage.removeItem('sslEnabled');
        this.setTheme(theme);
    }

    private getWebsocketUrl(): string {
        let protocol = 'ws://';
        if (this.isSslEnabled()) {
            protocol = 'wss://';
        }
        let url: string = protocol + this.getServerName();
        if (this.getServerPort()) {
            url = url + ':' + this.getServerPort();
        }
        url = url + '/api/websocket';
        return url;
    }

    private buildTopicName(): string {
        return '/topic/app/' + this.appId + '/node/' + this.getNodeId();
    }

    public showScreen(screen: any) {
        this.screen = screen;
        if (screen && screen.theme) {
            this.setTheme(screen.theme);
        }
        this.screenSource.next(screen);
    }

    public showDialog(dialogObj: any) {
        if (!dialogObj) {
            this.dialog = null;
        } else if (dialogObj.template.dialog) {
            console.log(`SessionService.showDialog invoked. dialogObj: ${dialogObj}`);
            this.dialog = dialogObj;
            this.response = null;
        }
        this.dialogSource.next(this.dialog);
    }

    public getPersonalizationScreen(): any {
        // tslint:disable-next-line:max-line-length
        return { type: 'Personalization', sequenceNumber: Math.floor(Math.random() * 2000), name: 'Device Setup', refreshAlways: true, template: { type: 'Blank', dialog: false } };
    }

    public getTheme(): string {
        if (this.screen && this.screen.theme) {
            return this.screen.theme;
        } else {
            return localStorage.getItem('theme');
        }
    }

    public setAuthToken(token: string) {
        this.authToken = token;
    }

    public isSslEnabled(): boolean {
        return 'true' === localStorage.getItem('sslEnabled');
    }

    public setSslEnabled(enabled: boolean) {
        localStorage.setItem('sslEnabled', enabled + '');
    }

    public setTheme(theme: string) {
        localStorage.setItem('theme', theme);
        if (this.previousTheme !== theme) {
            console.log(`Theme changing from '${this.previousTheme}' to '${theme}'`);
            this.onThemeChanging.emit({currentTheme: this.previousTheme, newTheme: theme});
            this.previousTheme = theme;          
        }
    }

    public setServerName(name: string) {
        localStorage.setItem('serverName', name);
    }

    public getServerName(): string {
        return localStorage.getItem('serverName');
    }

    public getDeviceName(): string {
        return localStorage.getItem('deviceName');
    }

    public setDeviceName(deviceName: string): void {
        localStorage.setItem('deviceName', deviceName);
    }

    public getAppId(): String {
        return this.appId;
    }

    public setServerPort(port: string) {
        localStorage.setItem('serverPort', port);
    }

    public getServerPort(): string {
        return localStorage.getItem('serverPort');
    }

    public getNodeId(): string {
        return localStorage.getItem('nodeId');
    }

    public setNodeId(id: string) {
        localStorage.setItem('nodeId', id);
    }

    public refreshApp() {
        window.location.href = 'index.html';
    }

    public isPersonalized(): boolean {
        if (this.getServerName() && this.getNodeId() && this.getServerPort()) {
            return true;
        } else {
            return false;
        }
    }

    public connected(): boolean {
        return this.stompService && this.stompService.connected();
    }

    public subscribe(appName: String) {
        if (this.subscribed) {
            return;
        }

        const url = this.getWebsocketUrl();
        console.log('creating new stomp service at: ' + url);
        this.stompService = new StompService({
            url: url,
            headers: {
                authToken: this.authToken,
            },
            heartbeat_in: 0, // Typical value 0 - disabled
            heartbeat_out: 20000, // Typical value 20000 - every 20 seconds
            reconnect_delay: 5000,
            debug: this.stompDebug
        });

        // Give preference to nodeId query parameter if it's present, then fallback to
        // local storage
        this.appId = appName;
        const currentTopic = this.buildTopicName();

        console.log('subscribing to server at: ' + currentTopic);

        this.messages = this.stompService.subscribe(currentTopic as string);

        // Subscribe a function to be run on_next message
        this.subscription = this.messages.subscribe(this.onNextMessage);

        this.state = this.stompService.state
            .map((state: number) => StompState[state]);

        this.subscribed = true;
        this.onServerConnectObserver.next(true);
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
            this.stompService.publish(`/app/device/app/${this.appId}/node/${this.getNodeId()}/device/${deviceResponse.deviceId}`,
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

    public async onAction(action: string | IMenuItem, payload?: any, confirm?: string, isValueChangedAction?: boolean) {
        if (action) {
            let actionString = '';
            // we need to figure out if we are a menuItem or just a string
            if (action.hasOwnProperty('action')) {
                const menuItem = <IMenuItem>(action);
                confirm = menuItem.confirmationMessage;
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
                const dialogRef = this.dialogService.open(ConfirmationDialogComponent, { disableClose: true });
                dialogRef.componentInstance.title = confirm;
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

            if (processAction && !this.loading) {
                const sendToServer: Function = () => {
                    console.log(`>>> Post action "${actionString}"`);
                    this.publish(actionString);
                };

                // see if we have any intercepters registered
                // otherwise just send the action
                if (this.actionIntercepters.has(actionString)) {
                    this.actionIntercepters.get(actionString).intercept(this.response, sendToServer);
                } else {
                    sendToServer();
                    if (!isValueChangedAction) {
                        this.showDialog(null);
                    }
                    this.queueLoading();
                }
            } else {
                console.log(`Not sending action: ${actionString}.  processAction: ${processAction}, loading:${this.loading}`);
            }

        } else {
            console.log(`received an invalid action: ${action}`);
        }
    }

    public keepAlive() {
        console.log(`>>> KeepAlive`);
        this.publish('KeepAlive');
    }

    private publish(actionString: string) {
        this.stompService.publish('/app/action/app/' + this.appId + '/node/' + this.getNodeId(),
            JSON.stringify({ name: actionString, data: this.response }));
    }

    private queueLoading() {
        this.loading = true;
        setTimeout(() => this.showLoading(LoaderState.LOADING_TITLE), 1000);
    }

    private showLoading(title: string, message?: string) {
        if (this.loading) {
            this.loaderState.setVisible(true, title, message);
        }
    }

    public cancelLoading() {
        this.loading = false;
        this.loaderState.setVisible(false);
    }

    /** Consume a message from the stompService */
    public onNextMessage = (message: Message) => {
        const json = JSON.parse(message.body);
        if (json.clearDialog) {
            this.showDialog(null);
        } else {
            if (json.type === 'Loading') { // This is just a temporary hack
                this.loading = true;
                this.showLoading(json.title, json.message);
                return;
            } else if (json.type === 'DevTools') {
                this.devToolRouter(json);
            } else if (json.template && json.template.dialog) {
                this.showDialog(json);
            } else if (json.type === 'NoOp') {
                this.response = null;
                return; // As with DeviceRequest, return to avoid dismissing loading screen
            } else if (json.type === 'DeviceRequest') {
                this.onDeviceRequest.emit(json);
                // Return explicitly in the case that the prior
                // screen shown was a 'loading' screen, don't want to dismiss
                // that prematurely
                return;
            } else if( json.type === 'Toast') {
                const toast = json as IToastScreen;
                this.snackBar.open(toast.message, null, {
                    duration:toast.duration,
                    panelClass: this.getToastClass(toast.toastType)
                  });
            } else {
                this.response = null;
                this.showScreen(json);
                this.showDialog(null);
            }
            this.cancelLoading();
        }
    }

    private devToolRouter(json) {
        if (json.name === 'DevTools::Get') {
            this.populateDevTables(json);
        }
    }

    private getToastClass( type: ToastType ): string {
        switch(type){
            case ToastType.Success:
                return 'toast-success';
            case ToastType.Warn:
                return 'toast-warn';
        }

        return null;
    }

    private populateDevTables(json: any) {
        if(json.currentState) {
            console.log("Pulling current state actions...");
            this.currentState = json.currentState.stateName;
            this.subState$.next(this.currentState);
            this.currentStateClass = json.currentState.stateClass;
            this.subStateClass$.next(this.currentStateClass);
            this.currentStateActions = [];
            for(var i = 0; i < json.actionsSize; i = i + 2) {
                this.currentStateActions.push({
                    Action: json.actions[i],
                    Destination: json.actions[i+1]

                });
                this.subStateActions$.next(this.currentStateActions);
                
            }
        }
        if(json.scopes.ConversationScope) {
            console.log('Pulling Conversation Scope Elements...');
            this.ConvElements = [];
            json.scopes.ConversationScope.forEach(element => {
                if (!this.ConvElements.includes(element, 0)) {
                    this.ConvElements.push({
                        ID: element.name,
                        Time: element.date,
                        StackTrace: element.stackTrace,  
                        Value: element.value 
                    });
                    this.subConv$.next(this.ConvElements);
                }
            });
            
        }
        if(json.scopes.SessionScope) {
            console.log('Pulling Session Scope Elements...');
            this.SessElements = [];
            json.scopes.SessionScope.forEach(element => {
                if (!this.SessElements.includes(element, 0)) {
                    this.SessElements.push({
                        ID: element.name,
                        Time: element.date,
                        StackTrace: element.stackTrace,  
                        Value: element.value 
                    });
                    this.subSess$.next(this.SessElements);
                }
            });
            
        }
        if(json.scopes.NodeScope) {
            console.log('Pulling Node Scope Elements...');
            this.NodeElements = [];
            json.scopes.NodeScope.forEach(element => {
                if (!this.NodeElements.includes(element, 0)) {
                    this.NodeElements.push({
                        ID: element.name,
                        Time: element.date,
                        StackTrace: element.stackTrace,  
                        Value: element.value 
                    });
                    this.subNode$.next(this.NodeElements);
                }
            });
            
        }
        if(json.scopes.FlowScope) {
            console.log('Pulling Flow Scope Elements...');
            this.FlowElements = [];
            json.scopes.FlowScope.forEach(element => {
                if (!this.FlowElements.includes(element, 0)) {
                    this.FlowElements.push({
                        ID: element.name,
                        Time: element.date,
                        StackTrace: element.stackTrace,  
                        Value: element.value 
                    });
                    this.subFlow$.next(this.FlowElements);
                }
            });
            console.log(this.FlowElements);
        }

        if(json.scopes.ConfigScope) {
            console.log('Pulling Config Scope Elements...');
            this.ConfElements = [];
            json.scopes.ConfigScope.forEach(element => {
                if (!this.ConfElements.includes(element, 0)) {
                    this.ConfElements.push({
                        ID: element.name,
                        Time: element.date,
                        StackTrace: element.stackTrace,  
                        Value: element.value 
                    });
                    this.subConf$.next(this.ConfElements);
                }
            });
            console.log(this.ConfElements);
        }

        if(json.saveFiles) {
            console.log('Pulling save files...');
            this.savePoints = [];
            json.saveFiles.forEach(saveName => {
                this.savePoints.push(saveName);
                this.subSave$.next(this.savePoints);
                console.log(this.savePoints);
            })
        }
    }

    public addSaveFile(newSavePoint: string) {
        if (newSavePoint) {
            if(!this.savePoints.includes(newSavePoint)) {
                this.savePoints.push(newSavePoint);
                this.subSave$.next(this.savePoints);
            }
          this.onAction('DevTools::Save::' + newSavePoint);
          console.log("Save Point Created: \'" + newSavePoint + "\'");
        }
    }

    public removeSaveFile(saveName: string) {
        console.log('Attempting to remove Save Point \'' + saveName + '\'...');
        let index = this.savePoints.findIndex(item => {
            return saveName === item;
        });
        if (index !== -1) {
            this.onAction("DevTools::RemoveSave::" + saveName);
            this.savePoints.splice(index, 1);
            this.subSave$.next(this.savePoints);
            console.log('Save Points updated: ');
            console.log(this.savePoints);
        }
    }

    public removeNodeElement(element: Element) {
        console.log('Attempting to remove \'' + element.Value + '\'...');
        let index = this.NodeElements.findIndex(item => {
            return element.Value === item.Value;
        });
        if (index !== -1) {
            this.onAction("DevTools::Remove::Node", element);
            this.NodeElements.splice(index, 1);
            this.subNode$.next(this.NodeElements);
            console.log('Node Scope updated: ');
            console.log(this.NodeElements);
        }
    }

    public removeSessionElement(element: Element) {
        console.log('Attempting to remove \'' + element.Value + '\'...');
        let index = this.SessElements.findIndex(item => {
            return element.Value === item.Value;
        });
        if (index !== -1) {
            this.onAction("DevTools::Remove::Session", element);
            this.SessElements.splice(index, 1);
            this.subSess$.next(this.SessElements);
            console.log('Session Scope updated: ');
            console.log(this.NodeElements);
        }
    }

    public removeConversationElement(element: Element) {
        console.log('Attempting to remove \'' + element.Value + '\'...');
        let index = this.ConvElements.findIndex(item => {
            return element.Value === item.Value;
        });
        if (index !== -1) {
            this.onAction("DevTools::Remove::Conversation", element);
            this.ConvElements.splice(index, 1);
            this.subConv$.next(this.ConvElements);
            console.log('Conversation Scope updated: ');
            console.log(this.ConvElements);
        }
    }

    public removeConfigElement(element: Element) {
        console.log('Attempting to remove \'' + element.Value + '\'...');
        let index = this.ConfElements.findIndex(item => {
            return element.Value === item.Value;
        });
        if (index !== -1) {
            this.onAction("DevTools::Remove::Config", element);
            this.ConfElements.splice(index, 1);
            this.subConf$.next(this.ConfElements);
            console.log('Config Scope updated: ');
            console.log(this.ConfElements);
        }
    }

    public removeFlowElement(element: Element) {
        console.log('Attempting to remove \'' + element.Value + '\'...');
        let index = this.FlowElements.findIndex(item => {
            return element.Value === item.Value;
        });
        if (index !== -1) {
            this.onAction("DevTools::Remove::Flow", element);
            this.FlowElements.splice(index, 1);
            this.subFlow$.next(this.FlowElements);
            console.log('Flow Scope updated: ');
            console.log(this.FlowElements);
        }
    }

    private getUrlNodeId() {
        const urlPath = this.location.path();
        let urlNodeId = '';
        if (urlPath) {
            const urlTree = this.router.parseUrl(urlPath);
            if (urlTree) {
                if (urlTree.queryParams['nodeId']) {
                    urlNodeId = urlTree.queryParams['nodeId'];
                    console.log('nodeId found on query parameters: [' + urlNodeId + ']');
                }
            }
        }
        return urlNodeId;
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

    public getServerBaseURL(): string {
        if (!this.serverBaseUrl) {
            const protocol = this.isSslEnabled() ? 'https' : 'http';
            this.serverBaseUrl = `${protocol}://${this.getServerName()}${this.getServerPort() ? `:${this.getServerPort()}` : ''}`;
            console.log(`Generated serverBaseURL: ${this.serverBaseUrl}`);
        }
        return this.serverBaseUrl;
    }

    public getApiServerBaseURL(): string {
        return `${this.getServerBaseURL()}/api`;
    }

    getLocale(): string {
        let returnLocale: string;
        if (this.screen) {
            returnLocale = this.screen.locale;
        }
        return returnLocale || DEFAULT_LOCALE;
    }

}


