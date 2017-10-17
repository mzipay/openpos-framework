import { LoaderService } from './common/loader/loader.service';
import { IDialog } from './common/idialog';
import { Observable } from 'rxjs/Observable';
import { Message } from '@stomp/stompjs';
import { Subscription } from 'rxjs/Subscription';
import { Injectable } from '@angular/core';
import { StompService, StompState } from '@stomp/ng2-stompjs';
import { Location } from '@angular/common';
import { Router } from '@angular/router';

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

  constructor(private stompService: StompService, private location: Location, private router: Router, private loader: LoaderService) {
  }

  private buildTopicName(): String {
    return '/topic/app/' + this.appId + '/node/' + this.nodeId;
  }

  public connected(): boolean {
    return this.stompService.connected();
  }

  public subscribe(appName: String) {
    if (this.subscribed) {
      return;
    }

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


