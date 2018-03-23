import { IconService } from './../services/icon.service';
import { DomSanitizer } from '@angular/platform-browser';
import { ScreenDirective } from '../common/screen.directive';
import { IScreen } from '../common/iscreen';
import { ScreenService } from './../services/screen.service';
import { DialogComponent } from '../screens/dialog/dialog.component';
import { IMenuItem } from '../common/imenuitem';
import { Component, OnInit, OnDestroy, DoCheck, sequence, AfterViewInit, NgZone } from '@angular/core';
import { Type, ViewChild, ComponentFactory } from '@angular/core';
import { SessionService } from '../services/session.service';
import { StatusBarComponent } from '../screens/statusbar.component';
import { FocusDirective } from '../common/focus.directive';
import { Observable } from 'rxjs/Observable';
import { MatDialog, MatDialogRef, MatIconRegistry, MatSnackBar, MatSnackBarRef, SimpleSnackBar, MatDialogConfig } from '@angular/material';
import { OverlayContainer } from '@angular/cdk/overlay';
import { TemplateDirective } from './template.directive';
import { AbstractTemplate } from './abstract-template';
import { Router } from '@angular/router';
import { OpenPOSDialogConfig } from './idialog';

export abstract class AbstractApp implements OnDestroy, OnInit {

    private dialogRef: MatDialogRef<IScreen>;

    private previousScreenType: string;

    private previousDialogType: string;

    private dialogOpening: boolean;

    private previousScreenName: string;

    private snackBarRef: MatSnackBarRef<SimpleSnackBar>;

    private personalized: boolean;

    private registered: boolean;

    private installedScreen: IScreen;

    private template: AbstractTemplate;

    @ViewChild(TemplateDirective) host: TemplateDirective;

    constructor(public screenService: ScreenService,
        public session: SessionService,
        public dialog: MatDialog,
        public iconService: IconService,
        public snackBar: MatSnackBar,
        public overlayContainer: OverlayContainer,
        protected router: Router) {
    }

    ngOnInit(): void {

        const self = this;
        this.session.subscribeForScreenUpdates((screen: any): void => self.updateTemplateAndScreen(screen));
        this.session.subscribeForDialogUpdates((dialog: any): void => self.updateDialog(dialog));

        if (!this.registerWithServer()) {
            this.updateTemplateAndScreen();
        }
    }

    public registerWithServer(): boolean {
        if (!this.registered && this.isPersonalized()) {
            console.log('initializing the application');
            this.session.unsubscribe();
            this.session.subscribe(this.router.url.substring(1));
            this.registered = true;
        }
        return this.registered;
    }

    getTheme(): string {
        if (this.session.screen && this.session.screen.theme) {
            localStorage.setItem('theme', this.session.screen.theme);
            return this.session.screen.theme;
        } else if (localStorage.getItem('theme')) {
            return localStorage.getItem('theme');
        } else {
            return 'openpos-theme';
        }
    }

    ngOnDestroy(): void {
        this.session.unsubscribe();
    }

    isPersonalized(): boolean {
        if (!this.personalized && this.session.isPersonalized()) {
            this.personalized = true;
            console.log('already personalized.  setting needs personalization to false');
        }
        return this.personalized;
    }

    public updateDialog(dialog?: any): void {
        this.registerWithServer();
        if (dialog) {
            const dialogType = this.screenService.hasScreen(dialog.subType) ? dialog.subType : 'Dialog';
            if (!this.dialogOpening && (!this.dialogRef || this.previousDialogType !== dialogType)) {
                if (this.dialogRef) {
                    console.log('closing dialog');
                    this.dialogRef.close();
                    this.dialogRef = null;
                }
                console.log('opening dialog \'' + dialogType + '\'');
                this.dialogOpening = true;
                setTimeout(() => this.openDialog(dialog), 0);
            } else {
                console.log(`Not opening dialog! Here's why: dialogOpening? ${this.dialogOpening}, dialogRef: ${this.dialogRef}, ` +
                `dialogType: ${dialogType}, previousDialogType: ${this.previousDialogType}`);
            }
        } else if (!dialog && this.dialogRef) {
            console.log('closing dialog');
            this.dialogRef.close();
            this.dialogRef = null;
        }
    }

    updateTemplateAndScreen(screen?: any): void {
        this.registerWithServer();

        if (!this.isPersonalized() && !this.session.screen) {
            console.log('setting up the personalization screen');
            this.session.screen = this.session.getPersonalizationScreen();
        } else if (!this.session.screen) {
            this.session.screen = { type: 'Blank', template: 'Blank' };
        }

        if (this.session.screen &&
            ((this.session.screen.refreshAlways)
                || this.session.screen.type !== this.previousScreenType
                || this.session.screen.name !== this.previousScreenName
            )
        ) {

            let templateName: string = null;
            let screenType: string = null;
            let screenName: string = null;
            if (this.session.screen && this.session.screen.type) {
                console.log(`Switching screens from ${this.previousScreenType} to ${this.session.screen.type}`);
                templateName = this.session.screen.template;
                screenType = this.session.screen.type;
                screenName = this.session.screen.name;
            }
            const templateComponentFactory: ComponentFactory<IScreen> = this.screenService.resolveScreen(templateName);
            const viewContainerRef = this.host.viewContainerRef;
            viewContainerRef.clear();
            this.template = viewContainerRef.createComponent(templateComponentFactory).instance as AbstractTemplate;
            this.previousScreenType = screenType;
            this.previousScreenName = screenName;
            this.overlayContainer.getContainerElement().classList.add(this.getTheme());
            this.installedScreen = this.template.installScreen(this.screenService.resolveScreen(screenType), this.session, this);

        }            
        this.template.show(screen, this);
        this.installedScreen.show(screen, this, this.template);

    }

    openDialog(dialog: any) {
        const dialogComponentFactory: ComponentFactory<IScreen> = this.screenService.resolveScreen(dialog.subType);
        let dialogComponent = this.screenService.resolveScreen('Dialog').componentType;
        this.previousDialogType = 'Dialog';
        const dialogProperties: OpenPOSDialogConfig = { disableClose: true };

        // if we resolved a specific screen type use that otherwise just use the default DialogComponent
        if (dialogComponentFactory) {
            dialogComponent = dialogComponentFactory.componentType;
            this.previousDialogType = dialog.subType;
        }
        if (dialog.dialogProperties) {
            // Merge in any dialog properties provided on the screen
            for (const key in dialog.dialogProperties) {
                if (dialog.dialogProperties.hasOwnProperty(key)) {
                    dialogProperties[key] = dialog.dialogProperties[key];
                }
            }
            console.log(`Dialog options: ${JSON.stringify(dialogProperties)}`);
        }

        this.dialogRef = this.dialog.open(dialogComponent, dialogProperties);
        this.dialogRef.componentInstance.show(dialog, this);
        this.dialogOpening = false;
        console.log('Dialog \'' + this.previousDialogType + '\' opened');
        if (dialogProperties.executeActionBeforeClose) {
            // Some dialogs may need to execute the chosen action before
            // they close so that actionPayloads can be included with the action
            // before the dialog is destroyed.
            this.dialogRef.beforeClose().subscribe(result => {
                this.session.onAction(result);
            });
        }

        this.dialogRef.afterClosed().subscribe(result => {
                if (!dialogProperties.executeActionBeforeClose) {
                    this.session.onAction(result);
                }
                this.dialogRef = null;
            }
        );
    }
}

