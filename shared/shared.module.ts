import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // for material
import { FlexLayoutModule } from '@angular/flex-layout';
import { HttpClientModule } from '@angular/common/http';
import { TextMaskModule } from 'angular2-text-mask';
import { SearchSelectModule } from '@oasisdigital/angular-material-search-select';
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
    ScreenDirective,
    TemplateDirective,
    AutoSelectOnFocusDirective,
    RequireAtleastOneValidatorDirective,
    PhoneUSValidatorDirective
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
    CheckoutListItemComponent,
    FabToggleButtonComponent,
    FabToggleGroupComponent,
    PopTartComponent,
    NavListComponent,
    FileViewerComponent
} from './components';
import { SafePipe } from './pipes/safe.pipe';
import { PhonePipe } from './pipes';

const components = [
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
    CheckoutListItemComponent,
    FabToggleButtonComponent,
    FabToggleGroupComponent,
    PopTartComponent,
    NavListComponent,
    FileViewerComponent
]

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
    TemplateDirective,
    ScreenDirective,
    RequireAtleastOneValidatorDirective,
    PhoneUSValidatorDirective
]

const pipes = [
    SafePipe,
    PhonePipe
]

@NgModule({
    entryComponents: [],
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
        SearchSelectModule
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
        SearchSelectModule,

        ...directives,
        ...components,
        ...pipes
    ],
    providers: [
    ]
})
export class SharedModule {}
