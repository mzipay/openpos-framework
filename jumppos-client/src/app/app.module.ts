import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {NgbModule, NgbCollapseModule, NgbModal, ModalDismissReasons} from '@ng-bootstrap/ng-bootstrap';
import {StompConfig, StompService} from '@stomp/ng2-stompjs';

import {AppComponent} from './app.component';

import {AppRoutingModule} from './app-routing.module';

import {SessionService} from './session.service';
import {PromptComponent} from './screens/prompt.component';
import {SellComponent} from './screens/sell.component';
import {DialogComponent} from './screens/dialog.component';
import {FormComponent} from './screens/form.component';
import {PaymentStatusComponent} from './screens/payment-status.component';
import {SellItemDetailComponent} from './screens/sell-item-detail.component';
import { FocusDirective } from './screens/focus';
import { Angular2FontawesomeModule } from 'angular2-fontawesome/angular2-fontawesome';

const stompConfig: StompConfig = {
  // Which server?
  url: 'ws://localhost:8080/api/websocket',

  // Headers
  // Typical keys: login, passcode, host
  headers: {
    //    login: 'guest',
    //    passcode: 'guest'
  },

  // How often to heartbeat?
  // Interval in milliseconds, set to 0 to disable
  heartbeat_in: 0, // Typical value 0 - disabled
  heartbeat_out: 20000, // Typical value 20000 - every 20 seconds

  // Wait in milliseconds before attempting auto reconnect
  // Set to 0 to disable
  // Typical value 5000 (5 seconds)
  reconnect_delay: 5000,

  // Will log diagnostics on console
  debug: true
};

@NgModule({
  declarations: [
    AppComponent,
    PromptComponent,
    DialogComponent,
    SellComponent,
    SellItemDetailComponent,
    PaymentStatusComponent,
    FormComponent,
    FocusDirective
  ],
  imports: [
    BrowserModule,
    NgbModule.forRoot(),
    NgbCollapseModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    Angular2FontawesomeModule
  ],
  providers: [
    NgbModal,
    SessionService,
    StompService,
    {
      provide: StompConfig,
      useValue: stompConfig
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
