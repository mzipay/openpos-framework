import {Component, OnInit, OnDestroy} from '@angular/core';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {SessionService} from './session.service';
import {Observable} from 'rxjs/Observable';
import {Message} from '@stomp/stompjs';
import {Subscription} from 'rxjs/Subscription';
import {PromptComponent} from './screens/prompt.component';

import {StompService, StompState} from '@stomp/ng2-stompjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit, OnDestroy {

  // Subscription status
  private subscribed: boolean;
  public state: Observable<string>;

  // Stream of messages
  private subscription: Subscription;
  private messages: Observable<Message>;

  constructor(public session: SessionService, private stompService: StompService) {
  }

  ngOnInit(): void {
    this.subscribed = false;
    this.subscribe();
    this.state = this.stompService.state
      .map((state: number) => StompState[state]);
  }

  ngOnDestroy(): void {
  }

  subscribe() {
    if (this.subscribed) {
      return;
    }

    console.log('subscribing to server ...');

    this.messages = this.stompService.subscribe(
      '/topic/store/' + this.session.storeId + '/device/' + this.session.deviceId);

    // Subscribe a function to be run on_next message
    this.subscription = this.messages.subscribe(this.onNextMessage);

    this.subscribed = true;
  }

  unsubscribe() {
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

  onAction(action: String) {
    this.stompService.publish('/app/action/store/' + this.session.storeId + '/device/' + this.session.deviceId,
      JSON.stringify({name: action, data: this.session.response}));
  }

  /** Consume a message from the stompService */
  public onNextMessage = (message: Message) => {
    this.session.response = null;
    this.session.screen = JSON.parse(message.body);
  }


}
