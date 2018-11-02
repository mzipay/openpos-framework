import { DevMenuComponent } from './components/dynamic-screen/dev-menu.component';
import { SessionService } from './services/session.service';
import { PersonalizationStartupTask } from './components/startup/personalization-startup-task';
import { STARTUP_TASKS, STARTUP_COMPONENT, StartupService, STARTUP_FAILED_COMPONENT } from './services/startup.service';

// Angular Includes
import { NgModule, Injector, Optional, SkipSelf } from '@angular/core';
import { Location, LocationStrategy, PathLocationStrategy, DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { BreakpointObserver, MediaMatcher } from '@angular/cdk/layout';

import { SharedModule } from '../shared';
import { AppInjector } from './app-injector';
import {
    ConfirmationDialogComponent,
    PersonalizationComponent,
    DynamicScreenComponent,
    LoaderComponent,
    OpenposAppComponent
} from './components';
import { throwIfAlreadyLoaded } from './module-import-guard';
import { StartupComponent } from './components/startup/startup.component';
import { PersonalizationService } from './services/personalization.service';
import { DialogService } from './services/dialog.service';
import { StompRService } from '@stomp/ng2-stompjs';
import { SubscribeToSessionTask } from './components/startup/subscribe-to-session-task';
import { Router, RouterModule } from '@angular/router';
import { Logger } from './services/logger.service';
import { StartupFailedComponent } from './components/startup/startup-failed.component';
import { MatDialog } from '@angular/material';
import { FinalStartupTask } from './components/startup/final-startup-task';
import { DialogContentComponent } from './components/dialog-content/dialog-content.component';
import { DialogServiceStartupTask } from './components/startup/dialog-service-startup-task';
import { ToastService } from './services';

@NgModule({
    entryComponents: [
        ConfirmationDialogComponent,
        PersonalizationComponent,
        StartupComponent,
        StartupFailedComponent,
        DialogContentComponent
    ],
    declarations: [
        OpenposAppComponent,
        DynamicScreenComponent,
        DevMenuComponent,
        DialogContentComponent,
        LoaderComponent,
        ConfirmationDialogComponent,
        PersonalizationComponent,
        StartupComponent,
        StartupFailedComponent
    ],
    imports: [
        SharedModule,
        RouterModule
    ],
    exports: [
        LoaderComponent,
        OpenposAppComponent
    ],
    providers: [
        HttpClient,
        Location,
        { provide: LocationStrategy, useClass: PathLocationStrategy },
        DatePipe,
        BreakpointObserver,
        MediaMatcher,
        StompRService,
        { provide: STARTUP_TASKS, useClass: PersonalizationStartupTask, multi: true, deps: [PersonalizationService, MatDialog]},
        { provide: STARTUP_TASKS, useClass: SubscribeToSessionTask, multi: true, deps: [SessionService, Router, Logger]},
        { provide: STARTUP_TASKS, useClass: DialogServiceStartupTask, multi: true, deps: [DialogService]},
        { provide: STARTUP_TASKS, useClass: FinalStartupTask, multi: true, deps: [SessionService]},
        { provide: STARTUP_COMPONENT, useValue: StartupComponent },
        { provide: STARTUP_FAILED_COMPONENT, useValue: StartupFailedComponent}
    ]
})
export class CoreModule {

    constructor(@Optional() @SkipSelf() parentModule: CoreModule, private injector: Injector, toastService: ToastService ) {
        throwIfAlreadyLoaded(parentModule, 'CoreModule');
        AppInjector.Instance = this.injector;
    }
}
