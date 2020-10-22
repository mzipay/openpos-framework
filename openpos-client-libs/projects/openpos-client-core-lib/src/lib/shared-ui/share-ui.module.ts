import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {FlexLayoutModule} from '@angular/flex-layout';
import {HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RouterModule} from '@angular/router';
import {TextMaskModule} from 'angular2-text-mask';

import {MatKeyboardModule} from '../keyboard/keyboard.module';
import {MaterialModule} from '../material/material.module';
import {SharedModule} from '../shared/shared.module';
import {CarouselComponent} from './components/carousel/carousel.component';

const screenParts = [

];

const components = [
    CarouselComponent,
];

const directives = [
];

const pipes = [
];

@NgModule({
    declarations: [
        ...directives,
        ...components,
        ...screenParts,
        ...pipes,
    ],
    entryComponents: [
    ],
    imports: [
        FormsModule,
        ReactiveFormsModule,
        RouterModule,
        HttpModule,
        HttpClientModule,
        FlexLayoutModule,
        CommonModule,
        MaterialModule,
        MatKeyboardModule,
        TextMaskModule,
        BrowserAnimationsModule,
        SharedModule
    ],
    exports: [
        FormsModule,
        ReactiveFormsModule,
        HttpModule,
        HttpClientModule,
        FlexLayoutModule,
        CommonModule,
        MaterialModule,
        MatKeyboardModule,
        TextMaskModule,
        ...directives,
        ...components,
        ...screenParts,
        ...pipes
    ]
})
export class SharedUIModule { }
