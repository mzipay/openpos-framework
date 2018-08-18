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
    StartupStatus
} from '../../services';
import { IScreen } from './screen.interface';
import { Element, OpenPOSDialogConfig, ActionMap, IMenuItem } from '../../interfaces';
import { FileViewerComponent, TemplateDirective } from '../../../shared';
import { PersonalizationService } from '../../services/personalization.service';

@Component({
    selector: 'app-dynamic-screen',
    templateUrl: './dynamic-screen.component.html',
    styleUrls: ['./dynamic-screen.component.scss'],
})
export class DynamicScreenComponent implements OnDestroy, OnInit {

    private showUpdating = false;

    private dialogRef: MatDialogRef<IScreen>;

    private previousScreenType: string;

    private dialogOpening: boolean;

    private previousScreenName: string;

    private snackBarRef: MatSnackBarRef<SimpleSnackBar>;

    private installedScreen: IScreen;

    private currentTemplateRef: ComponentRef<IScreen>;

    private installedTemplate: AbstractTemplate;

    private lastDialogType: string;

    public classes = '';

    private currentTheme: string;

    private disableDevMenu = false;

    @ViewChild(TemplateDirective) host: TemplateDirective;
    @ViewChild('myPanel') myPanel: MatExpansionPanel;
    matIcon = 'keyboard_arrow_down' || 'keyboard_arrow_up';

    screen: any;
    private lastSequenceNum: number;
    private lastScreenName: string;

    constructor(private personalization: PersonalizationService, public screenService: ScreenService, public dialogService: DialogService, public session: SessionService,
        public deviceService: DeviceService, public dialog: MatDialog,
        public iconService: IconService, public snackBar: MatSnackBar, public overlayContainer: OverlayContainer,
        protected router: Router, private pluginService: PluginService,
        private fileUploadService: FileUploadService,
        private httpClient: HttpClient, private cd: ChangeDetectorRef,
        private elRef: ElementRef, public renderer: Renderer2,
        private startupService: StartupService) {
    }

    ngOnInit(): void {
        const self = this;
        this.startupService.onStartupCompleted.subscribe(startupStatus => {
            if (startupStatus === StartupStatus.Success) {
                this.session.subscribeForScreenUpdates((screen: any): void => self.updateTemplateAndScreen(screen));
                this.session.subscribeForDialogUpdates((dialog: any): void => self.updateDialog(dialog));
            } else if (startupStatus === StartupStatus.Failure) {
                // If we failed, make sure we at least allow the Personalization screen to be shown
                this.session.subscribeForScreenUpdates((screen: any): void => {
                    if (screen && screen.screenType === 'Personalization' ) {
                        self.updateTemplateAndScreen(screen);
                    }
                });
            }
        });
        this.updateDialog({ screenType: 'Startup', template: { type: 'Blank', dialog: true, dialogProperties: { width: '60%',  panelClass: 'startup-dialog-container' } }});
    }


    ngOnDestroy(): void {
        this.session.unsubscribe();
    }

    protected updateDialog(dialog?: any): void {
        if (dialog) {
            const dialogType = this.dialogService.hasDialog(dialog.subType) ? dialog.subType : 'Dialog';
            if (!this.dialogOpening) {
                if (this.dialogRef && (dialog.screenType !== this.lastDialogType || dialog.screenType === 'Dialog')) {
                    console.log('closing dialog');
                    this.dialogRef.close();
                    this.dialogRef = null;
                }
                console.log('opening dialog \'' + dialogType + '\'');
                this.dialogOpening = true;
                setTimeout(() => this.openDialog(dialog), 0);
            } else {
                console.log(`Not opening dialog! Here's why: dialogOpening? ${this.dialogOpening}`);
            }
        } else if (!dialog && this.dialogRef) {
            console.log('closing dialog ref');
            this.dialogRef.close();
            this.dialogRef = null;
        }
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

    protected openDialog(dialog: any) {
        const dialogComponentFactory: ComponentFactory<IScreen> = this.dialogService.resolveDialog(dialog.screenType);
        let closeable = false;
        let closeAction = null;
        if (dialog.template.dialogProperties) {
            closeable = dialog.template.dialogProperties.closeable;
            closeAction = dialog.template.dialogProperties.closeAction;
        }
        // By default we want to not allow the user to close by clicking off
        // By default we need the dialog to grab focus so you cannont execute actions on the screen
        // behind by hitting enter
        const dialogProperties: OpenPOSDialogConfig = { disableClose: !closeable, autoFocus: true };
        const dialogComponent = dialogComponentFactory.componentType;
        if (dialog.template.dialogProperties) {
            // Merge in any dialog properties provided on the screen
            for (const key in dialog.template.dialogProperties) {
                if (dialog.template.dialogProperties.hasOwnProperty(key)) {
                    dialogProperties[key] = dialog.template.dialogProperties[key];
                }
            }
            console.log(`Dialog options: ${JSON.stringify(dialogProperties)}`);
        }

        if (!this.dialogRef || dialog.screenType !== this.lastDialogType || dialog.screenType === 'Dialog'
            || dialog.refreshAlways) {
            this.dialogRef = this.dialog.open(dialogComponent, dialogProperties);
        } else {
            console.log(`Using previously created dialogRef. current dialog type: ${dialog.screenType}, last dialog type: ${this.lastDialogType}`);
        }

        this.dialogRef.componentInstance.show(dialog, this);
        this.dialogOpening = false;
        console.log('Dialog \'' + dialog.screenType + '\' opened');
        if (dialogProperties.executeActionBeforeClose) {
            // Some dialogs may need to execute the chosen action before
            // they close so that actionPayloads can be included with the action
            // before the dialog is destroyed.
            this.dialogRef.beforeClose().subscribe(result => {
                this.session.onAction(closeAction || result);
            });
        } else {
            this.dialogRef.afterClosed().subscribe(result => {
                this.session.onAction(closeAction || result);
            }
            );
        }

        this.lastDialogType = dialog.screenType;
    }

    public get theme() {
       return this.personalization.getTheme();
    }

}
