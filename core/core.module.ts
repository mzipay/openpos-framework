// Angular Includes
import { NgModule, Injector, Optional, SkipSelf } from '@angular/core';
import { Location, LocationStrategy, PathLocationStrategy, DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { BreakpointObserver, MediaMatcher } from '@angular/cdk/layout';

import { SharedModule } from '../shared';
import { ScreenService } from './services';
import { AppInjector } from './app-injector';
import {
    ConfirmationDialogComponent,
    PersonalizationComponent,
    DynamicScreenComponent,
    LoaderComponent
 } from './components';
import { throwIfAlreadyLoaded } from './module-import-guard';
import { StartupComponent } from './components/startup/startup.component';

@NgModule({
    entryComponents: [
        ConfirmationDialogComponent,
        PersonalizationComponent,
        StartupComponent
    ],
    declarations: [
        DynamicScreenComponent,
        LoaderComponent,
        ConfirmationDialogComponent,
        PersonalizationComponent,
        StartupComponent
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

    constructor(@Optional() @SkipSelf() parentModule: CoreModule, screenService: ScreenService, private injector: Injector) {
        throwIfAlreadyLoaded(parentModule, 'CoreModule');
        screenService.addScreen('Personalization', PersonalizationComponent);
        screenService.addScreen('Startup', StartupComponent);
        AppInjector.Instance = this.injector;
    }
}
