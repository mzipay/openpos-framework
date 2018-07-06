// Angular Includes
import { NgModule, ErrorHandler } from '@angular/core';
import { Location, LocationStrategy, PathLocationStrategy, DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { BreakpointObserver, MediaMatcher } from '@angular/cdk/layout';

// Services
import { ScreenService } from './services/screen.service';
import { SessionService } from './services/session.service';
import { IconService } from './services/icon.service';
import { PluginService } from './services/plugin.service';
import { FormattersService } from './services/formatters.service';
import { FileUploadService } from './services/file-upload.service';
import { LocaleService, LocaleServiceImpl } from './services/locale.service';
import { ErrorHandlerService } from './services/errorhandler.service';

// Components
import { LoaderComponent } from './common/loader/loader.component';
import { ConfirmationDialogComponent } from './common/confirmation-dialog/confirmation-dialog.component';

import { DeviceService } from './services/device.service';
import { RequireAtleastOneValidatorDirective } from './common/validators/require-atleast-one.directive';

// On Screen Keyboard
import { ValidatorsService } from './services/validators.service';
import { DialogService } from './services/dialog.service';
import { PhoneUSValidatorDirective } from './common/validators/phone.directive';
import { SharedModule } from './shared';
import { DynamicScreenComponent, PersonalizationComponent } from './common';


@NgModule({
    entryComponents: [
        ConfirmationDialogComponent,
        PersonalizationComponent
    ],
    declarations: [
        DynamicScreenComponent,
        LoaderComponent,
        ConfirmationDialogComponent,
        RequireAtleastOneValidatorDirective,
        PhoneUSValidatorDirective,
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
        IconService,
        { provide: LocaleService, useClass: LocaleServiceImpl },
        SessionService,
        DeviceService,
        Location,
        { provide: LocationStrategy, useClass: PathLocationStrategy },
        DatePipe,
        BreakpointObserver,
        MediaMatcher,
        FormattersService,
        PluginService,
        FileUploadService,
        { provide: ErrorHandler, useClass: ErrorHandlerService }
    ]
})
// Export services below under 'providers'
export class OpenposCoreModule {

    constructor(screenService: ScreenService){
        screenService.addScreen('Personalization', PersonalizationComponent);
    }

    static forRoot() {
        return {
            ngModule: OpenposCoreModule,
            providers: [
                PluginService,
                ScreenService,
                DialogService,
                IconService,
                ValidatorsService
            ]
        };
    }
}
