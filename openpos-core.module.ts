import { PopTartComponent } from './dialogs/pop-tart/pop-tart.component';
import { NavListComponent } from './dialogs/nav-list/nav-list.component';

// Angular Includes
import { NgModule, ErrorHandler } from '@angular/core';
import { Location, LocationStrategy, PathLocationStrategy, DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { BreakpointObserver, MediaMatcher } from '@angular/cdk/layout';

// Pipes
import { PhonePipe } from './common/phone.pipe';

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

import { SelfCheckoutTransactionComponent } from './self-checkout/self-checkout-transaction/self-checkout-transaction.component';
import { SelfCheckoutHomeComponent } from './self-checkout/self-checkout-home/self-checkout-home.component';
import { SelfCheckoutStatusBarComponent } from './self-checkout/self-checkout-statusbar/self-checkout-statusbar.component';
import { SelfCheckoutPaymentStatusComponent } from './self-checkout/self-checkout-payment-status/self-checkout-payment-status.component';
import { DeviceService } from './services/device.service';
import { SelfCheckoutOptionsComponent } from './self-checkout/self-checkout-options/self-checkout-options.component';
import { RequireAtleastOneValidatorDirective } from './common/validators/require-atleast-one.directive';
import { AutoSelectOnFocus } from './common/autoSelect-onFocus.directive';

// On Screen Keyboard
import { ValidatorsService } from './services/validators.service';
import { DialogService } from './services/dialog.service';
import { PhoneUSValidatorDirective } from './common/validators/phone.directive';
import { SharedModule } from './shared';
import { DynamicScreenComponent, PersonalizationComponent } from './common';
import { SelfCheckoutWithBarComponent } from './self-checkout/selfcheckout-with-bar/selfcheckout-with-bar.component';


@NgModule({
    entryComponents: [
        SelfCheckoutHomeComponent,
        SelfCheckoutPaymentStatusComponent,
        SelfCheckoutTransactionComponent,
        ConfirmationDialogComponent,
        SelfCheckoutOptionsComponent,
        NavListComponent,
        PopTartComponent,
        SelfCheckoutWithBarComponent,
        PersonalizationComponent
    ],
    declarations: [
        DynamicScreenComponent,
        SelfCheckoutTransactionComponent,
        SelfCheckoutPaymentStatusComponent,
        LoaderComponent,
        SelfCheckoutHomeComponent,
        SelfCheckoutStatusBarComponent,
        ConfirmationDialogComponent,
        RequireAtleastOneValidatorDirective,
        AutoSelectOnFocus,
        SelfCheckoutOptionsComponent,
        NavListComponent,
        PopTartComponent,
        PhonePipe,
        PhoneUSValidatorDirective,
        SelfCheckoutWithBarComponent,
        PersonalizationComponent
    ],
    imports: [
        SharedModule
    ],
    exports: [
        LoaderComponent,
        PhonePipe
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
