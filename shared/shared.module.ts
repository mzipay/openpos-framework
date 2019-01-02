import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // for material
import { FlexLayoutModule } from '@angular/flex-layout';
import { HttpClientModule } from '@angular/common/http';
import { TextMaskModule } from 'angular2-text-mask';
import { MaterialModule } from './material.module';
import { MatKeyboardModule } from '../keyboard';

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
    KlassDirective
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
    StatusBarStatusControlComponent,
    STATUS_BAR_STATUS_CONTROL_COMPONENT,
    CurrencyTextComponent
} from './components';
import { SafePipe } from './pipes/safe.pipe';
import { PhonePipe, HourMinSecPipe, LocalizedDatePipe, ValueFormatterPipe, POSCurrencyPipe } from './pipes';
import { DynamicListControlComponent } from './components/dynamic-list-control/dynamic-list-control.component';
import { SideNavComponent } from './layout-components';
import { HeaderBarComponent } from './screen-parts';

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
    StatusBarStatusControlComponent,
    CatalogBrowserItemComponent,
    CurrencyTextComponent,
    HeaderBarComponent
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
    KlassDirective
];

const pipes = [
    SafePipe,
    PhonePipe,
    HourMinSecPipe,
    LocalizedDatePipe,
    ValueFormatterPipe,
    POSCurrencyPipe
];

@NgModule({
    entryComponents: [
        ...components
    ],
    declarations: [
        ...directives,
        ...components,
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
        TextMaskModule,
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
        ...pipes
    ],
    providers: [
        { provide: STATUS_BAR_STATUS_CONTROL_COMPONENT, useValue: StatusBarStatusControlComponent }
    ]
})
export class SharedModule {}
