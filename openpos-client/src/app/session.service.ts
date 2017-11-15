import { LoaderService } from './common/loader/loader.service';
import { IDialog } from './common/idialog';
import { Observable } from 'rxjs/Observable';
import { Message } from '@stomp/stompjs';
import { Subscription } from 'rxjs/Subscription';
import { Injectable } from '@angular/core';
import { StompService, StompState } from '@stomp/ng2-stompjs';
import { Location } from '@angular/common';
import { Router } from '@angular/router';

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

  private loading: boolean;

  private cordovaReady: boolean;

  private stompService: StompService;

  constructor(private location: Location, private router: Router, private loader: LoaderService) {
    document.addEventListener('deviceready', this.cordovaInitialized, false);
  }

  private cordovaInitialized() {
    console.log('devices are ready');
    this.cordovaReady = true;
  }

  public scan() {
    if (this.cordovaReady) {
      console.log('enabling scanning');
      cordova.plugins.barcodeScanner.scan(
        function (result) {
          alert('We got a barcode\n' +
            'Result: ' + result.text + '\n' +
            'Format: ' + result.format + '\n' +
            'Cancelled: ' + result.cancelled);
        },
        function (error) {
          alert('Scanning failed: ' + error);
        },
        {
          preferFrontCamera: false, // iOS and Android
          showFlipCameraButton: false, // iOS and Android
          showTorchButton: false, // iOS and Android
          torchOn: false, // Android, launch with the torch switched on (if available)
          saveHistory: false, // Android, save scan history (default false)
          prompt: 'Place a barcode inside the scan area', // Android
          resultDisplayDuration: 500, // Android, display scanned text for X ms. 0 suppresses it entirely, default 1500
          formats: 'CODE_128,EAN_8,EAN_13,UPC_A,UPC_E', // default: all but PDF_417 and RSS_EXPANDED
          orientation: 'landscape', // Android only (portrait|landscape), default unset so it rotates with the device
          disableAnimations: false, // iOS
          disableSuccessBeep: false // iOS and Android
        }
      );
    }
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

  private getServerName(): string {
    return localStorage.getItem('serverName');
  }

  private getServerPort(): string {
    return localStorage.getItem('serverPort');
  }

  private getNodeId(): string {
    return localStorage.getItem('nodeId');
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
    if (this.nodeId == null) {
      let temporaryNodeId = localStorage.getItem('temporaryNodeId');
      console.log('temporaryNodeId from local storage: ' + temporaryNodeId);
      if (temporaryNodeId == null) {
        const MIN = 999;
        const MAX = 9999990;
        temporaryNodeId = 'TEMPNODEID-' + Math.floor(Math.random() * (MAX - MIN + 1)) + MIN;
        console.log('GENERATING new temporaryNodeId: ' + temporaryNodeId);
        localStorage.setItem('temporaryNodeId', temporaryNodeId);
      }
      this.nodeId = temporaryNodeId;
    }

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

  public onAction(action: String) {
    if (action === 'SavePersonalization') {
      this.nodeId = this.response.formElements[0].value;
      localStorage.setItem('nodeId', '' + this.nodeId);
      localStorage.removeItem('temporaryNodeId');
      this.unsubscribe();
      this.subscribe(this.appId);
    } else {
      console.log('Publish action ' + action);
      this.stompService.publish('/app/action/app/' + this.appId + '/node/' + this.nodeId,
        JSON.stringify({ name: action, data: this.response }));
      this.dialog = null;
      this.queueLoading();
    }
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

  public onActionWithStringPayload(action: String, payload: String) {
    console.log('Publish action ' + action + ' with payload ' + payload);
    this.stompService.publish('/app/action/app/' + this.appId + '/node/' + this.nodeId,
      JSON.stringify({ name: action, data: payload }));
    this.queueLoading();
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
}


