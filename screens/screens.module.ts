// Angular Includes
import { NgModule } from '@angular/core';

import { SharedModule } from '../shared';
import { ScreenService, DialogService } from '../core';

// Screens
import { DynamicFormComponent } from './dynamic-form/dynamic-form.component';
import { TenderingComponent } from './tendering/tendering.component';
import { WarrantyCoverageComponent } from './warranty-coverage/warranty-coverage.component';
import { SaleRetrievalComponent } from './sale-retrieval/sale-retrieval.component';
import { EmbeddedWebPageComponent } from './embedded-web-page/embedded-web-page.component';
import { PromptComponent } from './prompt/prompt.component';
import { TransactionComponent } from './transaction/transaction.component';
import { BasicItemSearchComponent } from './basic-item-search/basic-item-search.component';
import { ChooseOptionsComponent } from './choose-options/choose-options.component';
import { PromptWithOptionsComponent } from './prompt-with-options/prompt-with-options.component';
import { PromptWithInfoComponent } from './prompt-with-info/prompt-with-info.component';
import { DialogComponent } from './dialog/dialog.component';
import { FormComponent } from './form/form.component';
import { HomeComponent } from './home/home.component';
import { PaymentStatusComponent } from './payment-status/payment-status.component';
import { SellItemDetailComponent } from './sell-item-detail/sell-item-detail.component';
import { SignatureCaptureComponent } from './signature-capture/signature-capture.component';
import { StaticTableComponent } from './static-table/static-table.component';
import { ItemListComponent } from './item-list/item-list.component';
import { LoginComponent } from './login/login.component';
import { OptionsComponent } from './options/options.component';
import { TillCountComponent } from './till-count/till-count.component';
import { TillCountOtherTenderComponent } from './till-count-other-tender/till-count-other-tender.component';
import { ChangeComponent } from './change/change.component';
import { WaitComponent } from './wait/wait.component';
import { CustomerSearchResultsComponent } from './customer-search-results/customer-search-results.component';
import { FullPageImageComponent } from './full-page-image/full-page-image.component';
import { CallForAuthorizationComponent } from './call-for-authorization/call-for-authorization.component';
import { MultipleDynamicFormComponent } from './multiple-dynamic-form/multiple-dynamic-form.component';
import { SelectionListComponent } from './selection-list/selection-list.component';
import { PrintPreviewComponent } from './print-preview/print-preview.component';

// Templates
import { BlankComponent } from './templates/blank/blank.component';
import { BlankWithBarComponent } from './templates/blank-with-bar/blank-with-bar.component';
import { SellComponent } from './templates/sell-template/sell/sell.component';
import { SellLinkedCustomerComponent } from './templates/sell-template/sell-linked-customer/sell-linked-customer.component';
import { StatusBarComponent } from './status-bar/status-bar.component';
import { TillSummaryComponent } from './till-summary/till-summary.component';
import { DynamicFormDialogComponent } from './dynamic-form/dynamic-form-dialog.component';
import { ChooseOptionsDialogComponent } from './choose-options/choose-options-dialog.component';
import { LoadingDialogComponent } from './loading-dialog/loading-dialog.component';
import { LoginDialogComponent } from './login/login-dialog.component';
import { MultipleDynamicFormDialogComponent } from './multiple-dynamic-form/multiple-dynamic-form-dialog.component';
import { VersionComponent } from './version/version.component';
import { ScreenConstants } from './screen.constants';

const screens = [
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
    PromptWithOptionsComponent,
    PromptWithInfoComponent,
    TransactionComponent,
    SellItemDetailComponent,
    SignatureCaptureComponent,
    StaticTableComponent,
    SaleRetrievalComponent,
    TenderingComponent,
    WarrantyCoverageComponent,
    OptionsComponent,
    TillSummaryComponent,
    TillCountComponent,
    TillCountOtherTenderComponent,
    DynamicFormComponent,
    ChangeComponent,
    PrintPreviewComponent,
    WaitComponent,
    CustomerSearchResultsComponent,
    FullPageImageComponent,
    CallForAuthorizationComponent,
    MultipleDynamicFormComponent,
    SelectionListComponent
]

const dialogs = [
    ChooseOptionsDialogComponent,
    DynamicFormDialogComponent,
    LoadingDialogComponent,
    LoginDialogComponent,
    MultipleDynamicFormDialogComponent,
    VersionComponent
]

const templates = [
    BlankComponent,
    BlankWithBarComponent,
    SellComponent,
    SellLinkedCustomerComponent,
]

const components = [
    StatusBarComponent
]

@NgModule({
    entryComponents: [
        ...screens,
        ...dialogs,
        ...templates
    ],
    declarations: [
        ...screens,
        ...dialogs,
        ...templates,
        ...components
    ],
    imports: [
        SharedModule
    ],
    exports: [

        ...templates,
        ...components
    ],
    providers: [
    ]
})
export class ScreensModule {
    constructor(screenService: ScreenService, dialogService: DialogService) {
        ScreenConstants.screens.forEach((screen) => {
            screenService.addScreen(screen.name, screen.component);
        });

        ScreenConstants.dialogs.forEach((dialog) => {
            dialogService.addDialog(dialog.name, dialog.component);
        });

        ScreenConstants.templates.forEach((template) => {
            screenService.addScreen(template.name, template.component);
        });
    }

 }
