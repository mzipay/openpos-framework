import { IDeviceResponse } from './../common/ideviceresponse';
import { IDeviceRequest } from './../common/idevicerequest';
import { IMenuItem } from '../common/imenuitem';
import { LoaderService } from '../common/loader/loader.service';
import { IDialog } from '../common/idialog';
import { Observable } from 'rxjs/Observable';
import { Message } from '@stomp/stompjs';
import { Subscription } from 'rxjs/Subscription';
import { Injectable, EventEmitter } from '@angular/core';
import { StompService, StompState } from '@stomp/ng2-stompjs';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { Scan } from '../common/scan';
import { FunctionActionIntercepter, ActionIntercepter } from '../common/actionIntercepter';

declare var cordova: any;

@Injectable()
export class SessionService {

  public screen: any;

  public dialog: IDialog;

  public response: any;

  private appId: String;

  public nodeId: String = '05243-013';

  private subscribed: boolean;

  public state: Observable<string>;

  private subscription: Subscription;

  private messages: Observable<Message>;

  public onDeviceRequest = new EventEmitter<IDeviceRequest>();

  private loading: boolean;

  private stompService: StompService;

  private actionPayloads: Map<string, Function> = new Map<string, Function>();
  private actionIntercepters: Map<string, ActionIntercepter> = new Map();

  constructor(private location: Location, private router: Router, private loader: LoaderService) {
  }

  public isRunningInBrowser(): boolean {
    const app = document.URL.indexOf( 'http://' ) === -1 && document.URL.indexOf( 'https://' ) === -1;
    return !app;
  }

  public personalize(serverName: string, serverPort: string, storeId: string, deviceId: string) {
    localStorage.setItem('serverName', serverName);
    localStorage.setItem('serverPort', serverPort);
    localStorage.setItem('nodeId', storeId + '-' + deviceId);
  }

  private getWebsocketUrl(): string {
    let url: string = 'ws://' + this.getServerName();
    if (this.getServerPort()) {
      url = url + ':' + this.getServerPort();
    }
    url = url + '/api/websocket';
    return url;
  }

  private buildTopicName(): string {
    return '/topic/app/' + this.appId + '/node/' + this.nodeId;
  }

  public getServerName(): string {
    return localStorage.getItem('serverName');
  }

  public getServerPort(): string {
    return localStorage.getItem('serverPort');
  }

  private getNodeId(): string {
    return localStorage.getItem('nodeId');
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

    this.stompService = new StompService({
      url: this.getWebsocketUrl(),
      headers: {
        //    login: 'guest',
        //    passcode: 'guest'
      },
      heartbeat_in: 0, // Typical value 0 - disabled
      heartbeat_out: 20000, // Typical value 20000 - every 20 seconds
      reconnect_delay: 5000,
      debug: true
    });

    // Give preference to nodeId query parameter if it's present, then fallback to
    // local storage
    const urlNodeId = this.getUrlNodeId();
    this.nodeId = urlNodeId ? urlNodeId : localStorage.getItem('nodeId');
    this.appId = appName;
    const currentTopic = this.buildTopicName();

    console.log('subscribing to server at ...' + currentTopic);

    this.messages = this.stompService.subscribe(currentTopic as string);

    // Subscribe a function to be run on_next message
    this.subscription = this.messages.subscribe(this.onNextMessage);

    this.state = this.stompService.state
      .map((state: number) => StompState[state]);

    this.subscribed = true;
  }

  public unsubscribe() {
    if (!this.subscribed) {
      return;
    }

    console.log('unsubscribing from server ...');

    // This will internally unsubscribe from Stomp Broker
    // There are two subscriptions - one created explicitly, the other created in the template by use of 'async'
    this.subscription.unsubscribe();
    this.subscription = null;
    this.messages = null;

    this.subscribed = false;
  }

  public onDeviceResponse(deviceResponse: IDeviceResponse) {
    console.log('Publish deviceResponse ' + deviceResponse);
    this.stompService.publish(`/app/device/app/${this.appId}/node/${this.nodeId}/device/${deviceResponse.deviceId}`,
      JSON.stringify(deviceResponse));
  }

  public onAction(action: string, payload?: any) {
      console.log('Publish action ' + action);

      // First we will use the payload passed into this function then
      // Check if we have registered action payload
      // Otherwise we will send whatever is in this.response
      if ( payload ) {
        this.response = payload;
      } else if ( this.actionPayloads.has( action ) ) {
        this.response = this.actionPayloads.get(action)();
      }

      let sendToServer: Function = () => {
        this.stompService.publish('/app/action/app/' + this.appId + '/node/' + this.nodeId,
        JSON.stringify({ name: action, data: this.response }))
      };

      // see if we have any intercepters registered
      // otherwise just send the action
      if( this.actionIntercepters.has( action ) ) {
        this.actionIntercepters.get( action ).intercept( this.response, sendToServer );
      } else {
        sendToServer();
      }

      this.dialog = null;
      this.queueLoading();
  }

  public onActionWithStringPayload(action: string, payload: any) {
    console.log('Publish action ' + action + ' with payload ' + payload);
    this.onAction( action, payload );
  }

  private queueLoading() {

    this.loading = true;
    setTimeout(() => this.showLoading(), 100);
  }

  private showLoading() {
    if (this.loading) {
      this.loader.show();
    }
  }

  private cancelLoading() {
    this.loading = false;
    this.loader.hide();
  }

  /** Consume a message from the stompService */
  public onNextMessage = (message: Message) => {
    const json = JSON.parse(message.body);
    if (json.clearDialog) {
      this.dialog = null;
    } else if (json.type === 'Dialog') {
      this.dialog = json;
    } else if (json.type === 'NoOp') {
      this.response = null;
    } else if (json.type === 'DeviceRequest') {
      this.onDeviceRequest.emit(json);
    } else {
      this.response = null;
      this.screen = json;
    }
    this.cancelLoading();
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

  public registerActionPayload( actionName: string, actionValue: Function ) {
    this.actionPayloads.set( actionName, actionValue );
  }

  public unregisterActionPayloads() {
    this.actionPayloads.clear();
  }

  public registerActionIntercepter( actionName: string, actionIntercepter: ActionIntercepter ){
    this.actionIntercepters.set( actionName, actionIntercepter );
  }

  public unregisterActionIntercepters() {
    this.actionIntercepters.clear();
  }

  public getCurrencyDenomination(): string {
      return 'USD';
  }

}
