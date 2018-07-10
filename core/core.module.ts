// Angular Includes
import { NgModule, Injector } from '@angular/core';
import { Location, LocationStrategy, PathLocationStrategy, DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { BreakpointObserver, MediaMatcher } from '@angular/cdk/layout';

import { SharedModule } from '../shared/shared.module';
import { ScreenService } from './services/screen.service';
import { AppInjector } from './app-injector';
import { ConfirmationDialogComponent } from './components/confirmation-dialog/confirmation-dialog.component';
import { PersonalizationComponent } from './components/personalization/personalization.component';
import { DynamicScreenComponent } from './components/dynamic-screen/dynamic-screen.component';
import { LoaderComponent } from './components/loader/loader.component';


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
