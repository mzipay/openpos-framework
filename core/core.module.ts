// Angular Includes
import { NgModule, Injector } from '@angular/core';
import { Location, LocationStrategy, PathLocationStrategy, DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { BreakpointObserver, MediaMatcher } from '@angular/cdk/layout';

import { SharedModule } from '../shared';
import { DynamicScreenComponent, PersonalizationComponent, LoaderComponent } from './components';
import { ScreenService } from './services';
import { ConfirmationDialogComponent } from './components';
import { AppInjector } from '../core/app-injector';


@NgModule({
    entryComponents: [
        ConfirmationDialogComponent,
        PersonalizationComponent
    ],
    declarations: [
        DynamicScreenComponent,
        LoaderComponent,
        ConfirmationDialogComponent,
        PersonalizationComponent
    ],
    imports: [
        SharedModule
    ],
    exports: [
        LoaderComponent
    ],
    providers: [
        HttpClient,
        Location,
        { provide: LocationStrategy, useClass: PathLocationStrategy },
        DatePipe,
        BreakpointObserver,
        MediaMatcher
    ]
})
export class CoreModule {

    constructor(screenService: ScreenService, private injector: Injector){
        screenService.addScreen('Personalization', PersonalizationComponent);
        AppInjector.Instance = this.injector;
    }
}
