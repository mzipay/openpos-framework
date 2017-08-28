import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { StompConfig, StompService } from '@stomp/ng2-stompjs';
import { Location, LocationStrategy, PathLocationStrategy } from '@angular/common';

import { AppComponent } from './app.component';
import { PosComponent } from './pos/pos.component';
import { KioskComponent } from './kiosk/kiosk.component';

import { AppRoutingModule } from './app-routing.module';

import { SessionService } from './session.service';
import { AbstractApp } from './screens/abstract-app';
import { PromptComponent } from './screens/prompt.component';
import { SellComponent } from './screens/sell.component';
import { ChooseOptionsComponent } from './screens/choose-options.component';
import { DialogComponent } from './screens/dialog.component';
import { FormComponent } from './screens/form.component';
import { HomeComponent } from './screens/home.component';
import { StatusBarComponent } from './screens/statusbar.component';
import { PaymentStatusComponent } from './screens/payment-status.component';
import { SellItemDetailComponent } from './screens/sell-item-detail.component';
import { FocusDirective } from './screens/focus';
import { Angular2FontawesomeModule } from 'angular2-fontawesome/angular2-fontawesome';
import { environment } from '../environments/environment';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // for material
import { FlexLayoutModule } from '@angular/flex-layout';
import { MdDialog, MdDialogRef } from '@angular/material';

import {
  MdAutocompleteModule,
  MdButtonModule,
  MdButtonToggleModule,
  MdCardModule,
  MdCheckboxModule,
  MdChipsModule,
  MdCoreModule,
  MdDatepickerModule,
  MdDialogModule,
  MdExpansionModule,
  MdGridListModule,
  MdIconModule,
  MdInputModule,
  MdListModule,
  MdMenuModule,
  MdNativeDateModule,
  MdPaginatorModule,
  MdProgressBarModule,
  MdProgressSpinnerModule,
  MdRadioModule,
  MdRippleModule,
  MdSelectModule,
  MdSidenavModule,
  MdSliderModule,
  MdSlideToggleModule,
  MdSnackBarModule,
  MdSortModule,
  MdTableModule,
  MdTabsModule,
  MdToolbarModule,
  MdTooltipModule
} from '@angular/material';
import 'hammerjs'; // for material

const stompConfig: StompConfig = {
  // Which server?
  url: environment.apiUrl,

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
  entryComponents: [
    DialogComponent
  ],
  declarations: [
    AppComponent,
    PosComponent,
    KioskComponent,
    PromptComponent,
    ChooseOptionsComponent,
    DialogComponent,
    SellComponent,
    SellItemDetailComponent,
    PaymentStatusComponent,
    FormComponent,
    HomeComponent,
    StatusBarComponent,
    FocusDirective
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    Angular2FontawesomeModule,
    MdButtonModule,
    MdCheckboxModule,
    MdCardModule,
    FlexLayoutModule,
    BrowserAnimationsModule,
    MdAutocompleteModule,
    MdButtonModule,
    MdButtonToggleModule,
    MdCardModule,
    MdCheckboxModule,
    MdChipsModule,
    MdCoreModule,
    MdDatepickerModule,
    MdDialogModule,
    MdExpansionModule,
    MdGridListModule,
    MdIconModule,
    MdInputModule,
    MdListModule,
    MdMenuModule,
    MdNativeDateModule,
    MdPaginatorModule,
    MdProgressBarModule,
    MdProgressSpinnerModule,
    MdRadioModule,
    MdRippleModule,
    MdSelectModule,
    MdSidenavModule,
    MdSliderModule,
    MdSlideToggleModule,
    MdSnackBarModule,
    MdSortModule,
    MdTableModule,
    MdTabsModule,
    MdToolbarModule,
    MdTooltipModule

  ],
  exports: [
    MdAutocompleteModule,
    MdButtonModule,
    MdButtonToggleModule,
    MdCardModule,
    MdCheckboxModule,
    MdChipsModule,
    MdCoreModule,
    MdDatepickerModule,
    MdDialogModule,
    MdExpansionModule,
    MdGridListModule,
    MdIconModule,
    MdInputModule,
    MdListModule,
    MdMenuModule,
    MdNativeDateModule,
    MdPaginatorModule,
    MdProgressBarModule,
    MdProgressSpinnerModule,
    MdRadioModule,
    MdRippleModule,
    MdSelectModule,
    MdSidenavModule,
    MdSliderModule,
    MdSlideToggleModule,
    MdSnackBarModule,
    MdSortModule,
    MdTableModule,
    MdTabsModule,
    MdToolbarModule,
    MdTooltipModule,
  ],
  providers: [
    MdDialog,
    SessionService,
    StompService,
    {
      provide: StompConfig,
      useValue: stompConfig
    },
    Location,
    {
      provide: LocationStrategy,
      useClass: PathLocationStrategy
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
