import { DevMenuComponent } from './components/dynamic-screen/dev-menu.component';
import { SessionService } from './services/session.service';
import { PersonalizationStartupTask } from './components/startup/personalization-startup-task';
import { StartupService } from './services/startup.service';
import { DialogService } from './services/dialog.service';
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
import { PersonalizationService } from './services/personalization.service';
import { StompRService } from '@stomp/ng2-stompjs';

@NgModule({
    entryComponents: [
        ConfirmationDialogComponent,
        PersonalizationComponent,
        StartupComponent
    ],
    declarations: [
        DynamicScreenComponent,
        DevMenuComponent,
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
        MediaMatcher,
        StompRService,
    ]
})
export class CoreModule {

    constructor(@Optional() @SkipSelf() parentModule: CoreModule, personalization: PersonalizationService, sessionService: SessionService,
        screenService: ScreenService, dialogService: DialogService,
        startupService: StartupService, private injector: Injector) {

        throwIfAlreadyLoaded(parentModule, 'CoreModule');
        screenService.addScreen('Personalization', PersonalizationComponent);
        dialogService.addDialog('Startup', StartupComponent);
        startupService.addStartupTask(new PersonalizationStartupTask(personalization, sessionService));
        AppInjector.Instance = this.injector;
    }
}
