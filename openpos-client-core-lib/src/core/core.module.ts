// Angular Includes
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { Location, LocationStrategy, PathLocationStrategy, DatePipe } from '@angular/common';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // for material
import { FlexLayoutModule } from '@angular/flex-layout';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { LayoutModule, BreakpointObserver, MediaMatcher } from '@angular/cdk/layout';

import { StompConfig, StompService } from '@stomp/ng2-stompjs';
import { TextMaskModule } from 'angular2-text-mask';
import 'hammerjs'; // for material

// Common
import { AbstractApp } from './common/abstract-app';

// Pipes
import { SafePipe } from './common/safe.pipe';

// Directives
import { ScreenDirective } from './common/screen.directive';
import { TemplateDirective } from './common/template.directive';
import { FocusDirective } from './common/focus.directive';

// Services
import { LoaderService } from './common/loader/loader.service';
import { ScreenService } from './services/screen.service';
import { SessionService } from './services/session.service';
import { IconService } from './services/icon.service';

// Components
import { IconComponent } from './common/controls/icon.component';
import { LoaderComponent } from './common/loader/loader.component';
import { DynamicFormControlComponent } from './common/controls/dynamic-form-control/dynamic-form-control.component';
import { ScanSomethingComponent } from './common/controls/scan-something/scan-something.component';
import { PosComponent } from './pos/pos.component';

// Screens
import { DynamicFormComponent } from './screens/dynamic-form/dynamic-form.component';
import { TenderingComponent } from './screens/tendering.component';
import { WarrantyCoverageComponent } from './screens/warranty-coverage.component';
import { SaleRetrievalComponent } from './screens/sale-retrieval.component';
import { EmbeddedWebPageComponent } from './screens/embedded-web-page.component';
import { PromptComponent } from './screens/prompt.component';
import { ProductListComponent } from './common/controls/product-list.component';
import { PromptInputComponent } from './common/controls/prompt-input.component';
import { TransactionComponent } from './screens/transaction/transaction.component';
import { BasicItemSearchComponent } from './screens/basic-item-search.component';
import { ChooseOptionsComponent } from './screens/choose-options.component';
import { PromptWithOptionsComponent } from './screens/prompt-with-options.component';
import { DialogComponent } from './screens/dialog.component';
import { FormComponent } from './screens/form.component';
import { HomeComponent } from './screens/home.component';
import { StatusBarComponent } from './screens/statusbar.component';
import { PaymentStatusComponent } from './screens/payment-status.component';
import { SellItemDetailComponent } from './screens/sell-item-detail.component';
import { SignatureCaptureComponent } from './screens/signature-capture.component';
//import { StaticTableComponent } from './screens/static-table.component';
import { ItemListComponent } from './screens/item-list.component';
import { LoginComponent } from './screens/login/login.component';
import { PersonalizationComponent } from './screens/personalization.component';
import { DeviceService } from './services/device.service';
import { OptionsComponent } from './screens/options/options.component';
import { TillSummaryComponent } from './screens/till/till-summary.component';
import { TillCountComponent } from './screens/till/till-count.component';
import { TillCountOtherTenderComponent } from './screens/till/till-count-other-tender.component';

// Templates
import { BlankComponent } from './templates/blank/blank.component';
import { SellComponent } from './templates/sell/sell.component';

import { MaterialModule } from './material.module';

@NgModule({
  entryComponents: [
    BasicItemSearchComponent,
    ChooseOptionsComponent,
    DialogComponent,
    EmbeddedWebPageComponent,
    FormComponent,
    HomeComponent,
    ItemListComponent,
    LoginComponent,
    PaymentStatusComponent,
    PromptComponent,
    PersonalizationComponent,
    PromptWithOptionsComponent,
    TransactionComponent,
    SellItemDetailComponent,
    SignatureCaptureComponent,
    //StaticTableComponent,
    SaleRetrievalComponent,
    TenderingComponent,
    WarrantyCoverageComponent,
    BlankComponent,
    SellComponent,
    OptionsComponent,
    TillSummaryComponent,
    TillCountComponent,
    TillCountOtherTenderComponent,
    DynamicFormComponent,
    DynamicFormControlComponent
  ],
  declarations: [
    DialogComponent,
    IconComponent,
    ProductListComponent,
    PromptComponent,
    PromptInputComponent,
    DynamicFormComponent,
    BasicItemSearchComponent,
    ItemListComponent,
    LoginComponent,
    ChooseOptionsComponent,
    PromptWithOptionsComponent,
    SignatureCaptureComponent,
    TransactionComponent,
    SellItemDetailComponent,
    PaymentStatusComponent,
    //StaticTableComponent,
    PersonalizationComponent,
    SaleRetrievalComponent,
    LoaderComponent,
    FormComponent,
    HomeComponent,
    StatusBarComponent,
    EmbeddedWebPageComponent,
    FocusDirective,
    ScreenDirective,
    SafePipe,
    TenderingComponent,
    WarrantyCoverageComponent,
    ScanSomethingComponent,
    BlankComponent,
    SellComponent,
    TemplateDirective,
    OptionsComponent,
    TillSummaryComponent,
    TillCountComponent,
    TillCountOtherTenderComponent,
    DynamicFormControlComponent,
    PosComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    HttpClientModule,
    FlexLayoutModule,
    BrowserAnimationsModule,
    MaterialModule,
    TextMaskModule
  ],
  exports: [
    PosComponent,
    LoaderComponent
    ],
  providers: [
    HttpClient,
    IconService,
    SessionService,
    DeviceService,
    LoaderService,
    ScreenService,
    Location,
    {
      provide: LocationStrategy,
      useClass: PathLocationStrategy
    },
    DatePipe,
    BreakpointObserver,
    MediaMatcher
  ]
})
export class CoreModule { }
