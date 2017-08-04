import {Observable} from 'rxjs/Observable';
import {Message} from '@stomp/stompjs';
import {Subscription} from 'rxjs/Subscription';
import {Injectable} from '@angular/core';
import {StompService, StompState} from '@stomp/ng2-stompjs';

@Injectable()
export class SessionService {

  public screen: any;

  public response: any;

  public nodeId: String = '05243013';

  private subscribed: boolean;

  public state: Observable<string>;

  private subscription: Subscription;
  private messages: Observable<Message>;

  constructor(private stompService: StompService) {
  }

  public subscribe() {
    if (this.subscribed) {
      return;
    }

    this.nodeId = localStorage.getItem('nodeId');
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

    console.log('subscribing to server at ...' + '/topic/node/' + this.nodeId);

    this.messages = this.stompService.subscribe(
      '/topic/node/' + this.nodeId);

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
    console.log('Publish action ' + action);
    this.stompService.publish('/app/action/node/' + this.nodeId,
      JSON.stringify({name: action, data: this.response}));

    if (action === 'SavePersonalization') {
      this.nodeId = this.response.formElements[0].value;
      localStorage.setItem('nodeId', '' + this.nodeId);
      localStorage.removeItem('temporaryNodeId');
      this.subscribed = false;
      this.subscribe();
    }
  }

    public onActionWithStringPayload(action: String, payload: String) {
    console.log('Publish action ' + action);
    this.stompService.publish('/app/action/node/' + this.nodeId,
      JSON.stringify({name: action, data: payload}));
  }

  /** Consume a message from the stompService */
  public onNextMessage = (message: Message) => {
    this.response = null;
    this.screen = JSON.parse(message.body);
  }

}
