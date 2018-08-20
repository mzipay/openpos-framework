import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Renderer2, ElementRef } from '@angular/core';
import { Component, ViewChild, HostListener, ComponentRef, OnDestroy, OnInit, ComponentFactory } from '@angular/core';
import { MatDialog, MatDialogRef, MatSnackBar,  MatSnackBarRef, SimpleSnackBar, MatExpansionPanel } from '@angular/material';
import { OverlayContainer } from '@angular/cdk/overlay';
import { Router } from '@angular/router';
import { AbstractTemplate } from '../abstract-template';
import { Configuration } from '../../../configuration/configuration';
import { IPlugin } from '../../plugins';
import {
    ScreenService,
    DialogService,
    SessionService,
    DeviceService,
    IconService,
    PluginService,
    FileUploadService,
    StartupService,
    StartupStatus,
    PersonalizationService,
    ToastService
} from '../../services';
import { IScreen } from './screen.interface';
import { Element, OpenPOSDialogConfig, ActionMap, IMenuItem } from '../../interfaces';
import { FileViewerComponent, TemplateDirective } from '../../../shared';

@Component({
    selector: 'app-dynamic-screen',
    templateUrl: './dynamic-screen.component.html',
    styleUrls: ['./dynamic-screen.component.scss'],
})
export class DynamicScreenComponent implements OnDestroy, OnInit {

    private showUpdating = false;

    private previousScreenType: string;

    private previousScreenName: string;

    private snackBarRef: MatSnackBarRef<SimpleSnackBar>;

    private installedScreen: IScreen;

    private currentTemplateRef: ComponentRef<IScreen>;

    private installedTemplate: AbstractTemplate;

    public classes = '';

    private currentTheme: string;

    private disableDevMenu = false;

    @ViewChild(TemplateDirective) host: TemplateDirective;
    @ViewChild('myPanel') myPanel: MatExpansionPanel;
    matIcon = 'keyboard_arrow_down' || 'keyboard_arrow_up';

    screen: any;
    private lastSequenceNum: number;
    private lastScreenName: string;

    constructor(
        private personalization: PersonalizationService,
        public screenService: ScreenService,
        public session: SessionService,
        public toast: ToastService,
        public deviceService: DeviceService,
        public iconService: IconService,
        public overlayContainer: OverlayContainer,
        protected router: Router,
        private pluginService: PluginService,
        private fileUploadService: FileUploadService,
        private httpClient: HttpClient,
        private cd: ChangeDetectorRef,
        private elRef: ElementRef,
        private startupService: StartupService,
        public renderer: Renderer2) {
    }

    ngOnInit(): void {
        const self = this;
        this.startupService.onStartupCompleted.subscribe(startupStatus => {
            if (startupStatus === StartupStatus.Success) {
                this.session.subscribeForScreenUpdates((screen: any): void => self.updateTemplateAndScreen(screen));
            } else if (startupStatus === StartupStatus.Failure) {
                // If we failed, make sure we at least allow the Personalization screen to be shown
                this.session.subscribeForScreenUpdates((screen: any): void => {
                    if (screen && screen.screenType === 'Personalization' ) {
                        self.updateTemplateAndScreen(screen);
                    }
                });
            }
        });
     }

    ngOnDestroy(): void {
        this.session.unsubscribe();
    }

    protected updateTemplateAndScreen(screen?: any): void {
        if (!screen) {
            screen = { screenType: 'Blank', template: { type: 'Blank', dialog: false } };
        }

        if (screen &&
            (screen.refreshAlways
                || screen.screenType !== this.previousScreenType
                || screen.name !== this.previousScreenName)
        ) {
            this.logSwitchScreens(screen);

            const templateName = screen.template.type;
            const screenType = screen.screenType;
            const screenName = screen.name;
            const templateComponentFactory: ComponentFactory<IScreen> = this.screenService.resolveScreen(templateName);
            const viewContainerRef = this.host.viewContainerRef;
            viewContainerRef.clear();
            if (this.currentTemplateRef) {
                this.currentTemplateRef.destroy();
            }
            this.currentTemplateRef = viewContainerRef.createComponent(templateComponentFactory);
            this.installedTemplate = this.currentTemplateRef.instance as AbstractTemplate;
            this.previousScreenType = screenType;
            this.previousScreenName = screenName;
            if (this.personalization.getTheme() !== this.currentTheme) {
                this.overlayContainer.getContainerElement().classList.remove(this.currentTheme);
                this.overlayContainer.getContainerElement().classList.add(this.personalization.getTheme());
                this.currentTheme = this.personalization.getTheme();
            }
            this.installedScreen = this.installedTemplate.installScreen(this.screenService.resolveScreen(screenType));
        }
        this.disableDevMenu = screen.template.disableDevMenu;
        this.installedTemplate.show(screen);
        this.installedScreen.show(screen, this, this.installedTemplate);

        this.updateClasses(screen);

    }

    protected logSwitchScreens(screen: any) {
        let msg = `>>> Switching screens from "${this.previousScreenType}" to "${screen.screenType}"`;
        let nameLogged = false;
        let sequenceLogged = false;
        if (screen.name && screen.name !== screen.screenType) {
            nameLogged = true;
            msg += ` (name "${screen.name}"`;
        }
        if (screen.sequenceNumber) {
            sequenceLogged = true;
            if (!nameLogged) {
                msg += ` (`;
            } else {
                msg += `, `;
            }
            msg += `sequence ${screen.sequenceNumber})`;
        }
        if (nameLogged && !sequenceLogged) {
            msg += `)`;
        }

        console.log(msg);
    }

    protected updateClasses(screen: any) {
        if (screen) {
            this.classes = '';
            switch (this.session.getAppId()) {
                case 'pos':
                    if (screen.screenType === 'Home') {
                        this.classes = 'pos main-background';
                    } else {
                        this.classes = 'pos';
                    }
                    break;
                case 'selfcheckout':
                    if (screen.screenType === 'SelfCheckoutHome') {
                        this.classes = 'self-checkout-home selfcheckout';
                    } else {
                        this.classes = 'selfcheckout';
                    }
                    break;
                case 'customerdisplay':
                    this.classes = 'selfcheckout';
                    break;
            }
        }
    }

    public get theme() {
       return this.personalization.getTheme();
    }

}
