import { Component, OnInit, OnDestroy } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import {Message} from '@stomp/stompjs';

import { Subscription } from 'rxjs/Subscription';
import {StompService} from '@stomp/ng2-stompjs';

@Component({
  selector: 'app-rawdata',
  templateUrl: './prototype.component.html',
  styleUrls: ['./prototype.component.css'],
  providers: []
})
export class PrototypeComponent implements OnInit, OnDestroy {

  // Stream of messages
  private subscription: Subscription;
  public messages: Observable<Message>;

  // Subscription status
  public subscribed: boolean;

  // A count of messages received
  public count = 0;

  private _counter = 1;

  public screen: JSON;

  /** Constructor */
  constructor(private _stompService: StompService) { }

  ngOnInit() {
    this.subscribed = false;

    // Store local reference to Observable
    // for use with template ( | async )
    this.subscribe();
  }

  public subscribe() {
    if (this.subscribed) {
      return;
    }

    // Stream of messages
    this.messages = this._stompService.subscribe('/topic/store/05243/device/013');

    // Subscribe a function to be run on_next message
    this.subscription = this.messages.subscribe(this.on_next);

    this.subscribed = true;
  }

  public unsubscribe() {
    if (!this.subscribed) {
      return;
    }

    // This will internally unsubscribe from Stomp Broker
    // There are two subscriptions - one created explicitly, the other created in the template by use of 'async'
    this.subscription.unsubscribe();
    this.subscription = null;
    this.messages = null;

    this.subscribed = false;
  }

  ngOnDestroy() {
    this.unsubscribe();
  }

  public onAction(action) {
    this._stompService.publish('/app/action/store/05243/device/013',
      action);
    this._counter++;
  }

  /** Consume a message from the _stompService */
  public on_next = (message: Message) => {

    // Count it
    this.count++;

    this.screen = JSON.parse(message.body);

    // Log it to the console
    console.log(this.screen);
  }
}
