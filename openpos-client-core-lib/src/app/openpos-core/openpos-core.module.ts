
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
import { IDeviceRequest } from './common/idevicerequest';
import { IDevicePlugin } from './common/idevice-plugin';

// Pipes
import { SafePipe } from './common/safe.pipe';

// Directives
import { ScreenDirective } from './common/screen.directive';
import { TemplateDirective } from './common/template.directive';
import { FocusDirective } from './common/focus.directive';

// Services
import { ScreenService } from './services/screen.service';
import { SessionService } from './services/session.service';
import { IconService } from './services/icon.service';
import { PluginService } from './services/plugin.service';
import { FormattersService } from './services/formatters.service';
import { FileUploadService } from './services/file-upload.service';
import { LocaleService, LocaleServiceImpl } from './services/locale.service';


// Components
import { IconComponent } from './common/controls/icon.component';
import { LoaderComponent } from './common/loader/loader.component';
import { DynamicFormControlComponent } from './common/controls/dynamic-form-control/dynamic-form-control.component';
import { ScanSomethingComponent } from './common/controls/scan-something/scan-something.component';
import { PosComponent } from './pos/pos.component';
import { SelfCheckoutComponent } from './self-checkout/self-checkout.component';
import { CustomerDisplayComponent } from './customerdisplay/customerdisplay.component';
import { ConfirmationDialogComponent } from './common/confirmation-dialog/confirmation-dialog.component';
import { CheckoutListItemComponent} from './common/controls/checkout-list-item/checkout-list-item.component';

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
import { SelfCheckoutTransactionComponent } from './self-checkout/self-checkout-transaction/self-checkout-transaction.component';
import { BasicItemSearchComponent } from './screens/basic-item-search.component';
import { ChooseOptionsComponent } from './screens/choose-options/choose-options.component';
import { PromptWithOptionsComponent } from './screens/prompt-with-options.component';
import { PromptWithInfoComponent } from './screens/prompt-with-info.component';
import { DialogComponent } from './screens/dialog/dialog.component';
import { FormComponent } from './screens/form.component';
import { HomeComponent } from './screens/home.component';
import { SelfCheckoutHomeComponent } from './self-checkout/self-checkout-home/self-checkout-home.component';
import { StatusBarComponent } from './screens/statusbar.component';
import { SelfCheckoutStatusBarComponent } from './self-checkout/self-checkout-statusbar/self-checkout-statusbar.component';
import { PaymentStatusComponent } from './screens/payment-status.component';
import { SelfCheckoutPaymentStatusComponent } from './self-checkout/self-checkout-payment-status/self-checkout-payment-status.component';
import { SellItemDetailComponent } from './screens/sell-item-detail.component';
import { SignatureCaptureComponent } from './screens/signature-capture.component';
import { StaticTableComponent } from './screens/static-table.component';
import { ItemListComponent } from './screens/item-list.component';
import { LoginComponent } from './screens/login/login.component';
import { PersonalizationComponent } from './screens/personalization.component';
import { DeviceService } from './services/device.service';
import { OptionsComponent } from './screens/options/options.component';
import { TillSummaryComponent } from './screens/till/till-summary.component';
import { TillCountComponent } from './screens/till/till-count.component';
import { TillCountOtherTenderComponent } from './screens/till/till-count-other-tender.component';
import { ChangeComponent } from './screens/change/change.component';
import { WaitComponent } from './screens/wait/wait.component';
import { CustomerSearchResultsComponent } from './screens/customer-search-results/customer-search-results.component';
import { FabToggleGroupComponent } from './common/controls/fab-toggle-group/fab-toggle-group.component';
import { FabToggleButtonComponent } from './common/controls/fab-toggle-button/fab-toggle-button.component';
import { SelfCheckoutLoyaltyComponent } from './self-checkout/self-checkout-loyalty/self-checkout-loyalty.component';
import { FullPageImageComponent } from './screens/full-page-image/full-page-image.component';

// Templates
import { BlankComponent } from './templates/blank/blank.component';
import { SellComponent } from './templates/sell/sell.component';
import { SelfCheckoutWithBarComponent } from './templates/selfcheckout-with-bar/selfcheckout-with-bar.component';

import { MaterialModule } from './material.module';
import { Plugin } from 'webpack';
import { httpInterceptorProviders } from './http-intercepters';
import { ShowErrorsComponent } from './common/controls/show-errors.component';
import { RequireAtleastOneValidatorDirective } from './common/validators/require-atleast-one.directive';
import { OverFlowListComponent } from './common/controls/overflow-list/overflow-list.component';
import { DynamicFormFieldComponent } from './common/controls/dynamic-form-field/dynamic-form-field.component';
import { MarkDirtyOnSubmit } from './common/mark-dirty-onSubmit.directive';
import { AutoSelectOnFocus } from './common/autoSelect-onFocus.directive';
import { FormattedInputValueAccessor } from './common/input-formatter.directive';

// On Screen Keyboard
import { MatKeyboardModule } from '@ngx-material-keyboard/core';
import { KeyboardDirective } from './common/keyboard.directive';
import { ValidatorsService } from './services/validators.service';
import { PrintPreviewComponent } from './screens/print-preview.component';
import { MatExclusiveSelectionListDirective } from './common/mat-exclusive-selection-list.directive';

@NgModule({
  entryComponents: [
    BasicItemSearchComponent,
    ChooseOptionsComponent,
    DialogComponent,
    EmbeddedWebPageComponent,
    FormComponent,
    HomeComponent,
    SelfCheckoutHomeComponent,
    ItemListComponent,
    LoginComponent,
    PaymentStatusComponent,
    SelfCheckoutPaymentStatusComponent,
    PromptComponent,
    PersonalizationComponent,
    PromptWithOptionsComponent,
    PromptWithInfoComponent,
    TransactionComponent,
    SelfCheckoutTransactionComponent,
    SellItemDetailComponent,
    SignatureCaptureComponent,
    StaticTableComponent,
    SaleRetrievalComponent,
    TenderingComponent,
    WarrantyCoverageComponent,
    ScanSomethingComponent,
    BlankComponent,
    SelfCheckoutWithBarComponent,
    SellComponent,
    OptionsComponent,
    TillSummaryComponent,
    TillCountComponent,
    TillCountOtherTenderComponent,
    DynamicFormComponent,
    DynamicFormControlComponent,
    DynamicFormFieldComponent,
    ChangeComponent,
    ConfirmationDialogComponent,
    CheckoutListItemComponent,
    PrintPreviewComponent,
    WaitComponent,
    CustomerSearchResultsComponent,
    SelfCheckoutLoyaltyComponent,
    FullPageImageComponent
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
    PromptWithInfoComponent,
    SignatureCaptureComponent,
    TransactionComponent,
    SelfCheckoutTransactionComponent,
    SellItemDetailComponent,
    PaymentStatusComponent,
    SelfCheckoutPaymentStatusComponent,
    StaticTableComponent,
    PersonalizationComponent,
    SaleRetrievalComponent,
    LoaderComponent,
    FormComponent,
    HomeComponent,
    SelfCheckoutHomeComponent,
    StatusBarComponent,
    SelfCheckoutStatusBarComponent,
    EmbeddedWebPageComponent,
    FocusDirective,
    ScreenDirective,
    SafePipe,
    TenderingComponent,
    WarrantyCoverageComponent,
    ScanSomethingComponent,
    BlankComponent,
    SelfCheckoutWithBarComponent,
    SellComponent,
    TemplateDirective,
    OptionsComponent,
    TillSummaryComponent,
    TillCountComponent,
    TillCountOtherTenderComponent,
    DynamicFormControlComponent,
    DynamicFormFieldComponent,
    PosComponent,
    SelfCheckoutComponent,
    CustomerDisplayComponent,
    ChangeComponent,
    ConfirmationDialogComponent,
    CheckoutListItemComponent,
    ShowErrorsComponent,
    RequireAtleastOneValidatorDirective,
    MarkDirtyOnSubmit,
    AutoSelectOnFocus,
    OverFlowListComponent,
    FormattedInputValueAccessor,
    KeyboardDirective,
    PrintPreviewComponent,
    WaitComponent,
    CustomerSearchResultsComponent,
    MatExclusiveSelectionListDirective,
    FabToggleGroupComponent,
    FabToggleButtonComponent,
    SelfCheckoutLoyaltyComponent,
    FullPageImageComponent
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
    MatKeyboardModule,
    TextMaskModule
  ],
  exports: [
    PosComponent,
    SelfCheckoutComponent,
    CustomerDisplayComponent,
    LoaderComponent,
    MaterialModule,
    MatKeyboardModule,
    IconComponent,
    ScreenDirective,
    ReactiveFormsModule,
    FabToggleGroupComponent,
    FabToggleButtonComponent
    ],
  providers: [
    HttpClient,
    IconService,
    { provide: LocaleService, useClass: LocaleServiceImpl },
    SessionService,
    DeviceService,
    Location,
    {
      provide: LocationStrategy,
      useClass: PathLocationStrategy
    },
    DatePipe,
    BreakpointObserver,
    MediaMatcher,
    httpInterceptorProviders,
    FormattersService,
    ValidatorsService,
    FileUploadService,
  ]
})
// Export services below under 'providers'
export class OpenposCoreModule {
  static forRoot() {
    return {
      ngModule: OpenposCoreModule,
      providers: [
        PluginService,
        ScreenService,
        IconService
      ]
    };
  }
}
