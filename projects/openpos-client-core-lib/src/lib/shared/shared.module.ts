import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // for material
import { FlexLayoutModule } from '@angular/flex-layout';
import { HttpClientModule } from '@angular/common/http';
import { TextMaskModule } from 'angular2-text-mask';
import { MaterialModule } from './material.module';
import { MatKeyboardModule } from '../keyboard/keyboard.module';

import {
    MarkDirtyOnSubmitDirective,
    InputFormatterDirective,
    FocusDirective,
    ScreenOrientationDirective,
    MimicScrollDirective,
    MatExclusiveSelectionListDirective,
    InactivityMonitorDirective,
    BarcodeScanPublisherDirective,
    AutoSelectOnFocusDirective,
    RequireAtleastOneValidatorDirective,
    PhoneUSValidatorDirective,
    OpenposScreenOutletDirective,
    ScreenDirective,
    AutocompleteDirective,
    DefaultImageDirective,
    HideFormAccessoryBarDirective,
    FindFloatingElementDirective,
    FixediOsScrollDirective,
    KlassDirective,
    ActionItemKeyMappingDirective,
    AutoCompleteAddressDirective,
    ArrowTabDirective,
    ArrowTabItemDirective
} from './directives';
import {
    PromptInputComponent,
    IconComponent,
    ShowErrorsComponent,
    DynamicDateFormFieldComponent,
    DynamicFormFieldComponent,
    DynamicFormControlComponent,
    ProductListComponent,
    SelectableItemListComponent,
    OverFlowListComponent,
    ScanSomethingComponent,
    ItemCardComponent,
    FabToggleButtonComponent,
    FabToggleGroupComponent,
    PopTartComponent,
    NavListComponent,
    FileViewerComponent,
    StatusBarComponent,
    CounterComponent,
    DatePartChooserComponent,
    DatePartChooserFieldComponent,
    DatePartChooserDialogComponent,
    PrimaryButtonComponent,
    SecondaryButtonComponent,
    TrainingDialogComponent,
    TrainingWrapperComponent,
    MessageDialogComponent,
    MenuComponent,
    TimeChooserComponent,
    PagerComponent,
    CatalogBrowserItemComponent,
    CurrencyTextComponent,
    SearchablePopTartComponent,
    IconButtonComponent,
    IconFabButtonComponent,
    ReceiptCardComponent,
    KebabButtonComponent,
    IconSquareButtonComponent,
    DisplayPropertyComponent,
    TransactionItemListComponent,
    GridTableComponent,
} from './components';
import { SafePipe } from './pipes/safe.pipe';
import { PhonePipe, HourMinSecPipe, LocalizedDatePipe, ValueFormatterPipe, POSCurrencyPipe, StringListFilterPipe, ListLimitPipe, MarkdownFormatterPipe, ImageUrlPipe, BackgroundImageUrlPipe } from './pipes';
import { DynamicListControlComponent } from './components/dynamic-list-control/dynamic-list-control.component';
import { BaconStripComponent, ScanOrSearchComponent, StatusStripComponent, SausageLinksComponent, DynamicFormPartComponent, DialogHeaderComponent } from './screen-parts';
import { SideNavComponent, WaffleComponent } from './layout-components';
import { KeyPressSourceDirective } from './directives/keypress-source.directive';
import { SystemStatusDialogComponent } from './components/system-status/system-status-dialog.component';
import { SellLinkedCustomerComponent } from '../screens-deprecated/templates/sell-template/sell-linked-customer/sell-linked-customer.component';
import { SellStatusSectionComponent } from '../screens-deprecated/templates/sell-template/sell-status-section/sell-status-section.component';
import { SaleFooterComponent } from '../screens-with-parts/sale/sale-footer/sale-footer.component';
import { SaleItemListComponent } from '../screens-with-parts/sale/sale-item-list/sale-item-list.component';

const screenParts = [
        DynamicFormPartComponent,
        StatusStripComponent,
        SausageLinksComponent,
        ScanOrSearchComponent,
        BaconStripComponent,
        DialogHeaderComponent,
        SaleFooterComponent,
        SaleItemListComponent
    ];

const components = [
    PromptInputComponent,
    IconComponent,
    ShowErrorsComponent,
    DynamicDateFormFieldComponent,
    DynamicFormFieldComponent,
    DynamicFormControlComponent,
    DynamicListControlComponent,
    ProductListComponent,
    SelectableItemListComponent,
    OverFlowListComponent,
    ScanSomethingComponent,
    ItemCardComponent,
    FabToggleButtonComponent,
    FabToggleGroupComponent,
    PopTartComponent,
    SearchablePopTartComponent,
    NavListComponent,
    FileViewerComponent,
    StatusBarComponent,
    CounterComponent,
    DatePartChooserComponent,
    DatePartChooserFieldComponent,
    DatePartChooserDialogComponent,
    PrimaryButtonComponent,
    SecondaryButtonComponent,
    SideNavComponent,
    TrainingDialogComponent,
    TrainingWrapperComponent,
    MessageDialogComponent,
    MenuComponent,
    TimeChooserComponent,
    PagerComponent,
    CatalogBrowserItemComponent,
    CurrencyTextComponent,
    WaffleComponent,
    IconButtonComponent,
    IconFabButtonComponent,
    ReceiptCardComponent,
    KebabButtonComponent,
    IconSquareButtonComponent,
    DisplayPropertyComponent,
    TransactionItemListComponent,
    GridTableComponent,
    SystemStatusDialogComponent,
    SellLinkedCustomerComponent,
    SellStatusSectionComponent
];

const directives = [
    MarkDirtyOnSubmitDirective,
    InputFormatterDirective,
    FocusDirective,
    ScreenOrientationDirective,
    MimicScrollDirective,
    MatExclusiveSelectionListDirective,
    InactivityMonitorDirective,
    BarcodeScanPublisherDirective,
    AutoSelectOnFocusDirective,
    RequireAtleastOneValidatorDirective,
    PhoneUSValidatorDirective,
    OpenposScreenOutletDirective,
    ScreenDirective,
    AutocompleteDirective,
    DefaultImageDirective,
    KlassDirective,
    HideFormAccessoryBarDirective,
    FindFloatingElementDirective,
    FixediOsScrollDirective,
    ActionItemKeyMappingDirective,
    AutoCompleteAddressDirective,
    KeyPressSourceDirective,
    ArrowTabDirective,
    ArrowTabItemDirective
];

const pipes = [
    SafePipe,
    PhonePipe,
    HourMinSecPipe,
    LocalizedDatePipe,
    ValueFormatterPipe,
    POSCurrencyPipe,
    StringListFilterPipe,
    ListLimitPipe,
    MarkdownFormatterPipe,
    ImageUrlPipe,
    BackgroundImageUrlPipe
];

@NgModule({
    declarations: [
        ...directives,
        ...components,
        ...screenParts,
        ...pipes
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
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        HttpModule,
        HttpClientModule,
        FlexLayoutModule,
        BrowserAnimationsModule,
        MaterialModule,
        MatKeyboardModule,
        TextMaskModule,

        ...directives,
        ...components,
        ...screenParts,
        ...pipes
    ]
})
export class SharedModule {}
