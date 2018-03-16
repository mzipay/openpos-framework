import { DialogTemplateComponent } from './../templates/dialog/dialog.template.component';
import { IconService } from './../services/icon.service';
import { DomSanitizer } from '@angular/platform-browser';
import { ScreenDirective } from '../common/screen.directive';
import { IScreen } from '../common/iscreen';
import { ScreenService } from './../services/screen.service';
import { DialogComponent } from '../screens/dialog.component';
import { IMenuItem } from '../common/imenuitem';
import { Component, OnInit, OnDestroy, DoCheck, sequence, AfterViewInit, NgZone } from '@angular/core';
import { Type, ViewChild, ComponentFactory } from '@angular/core';
import { SessionService } from '../services/session.service';
import { StatusBarComponent } from '../screens/statusbar.component';
import { FocusDirective } from '../common/focus.directive';
import { Observable } from 'rxjs/Observable';
import { MatDialog, MatDialogRef, MatIconRegistry, MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material';
import { OverlayContainer } from '@angular/cdk/overlay';
import { TemplateDirective } from './template.directive';
import { AbstractTemplate } from './abstract-template';
import { Router } from '@angular/router';

export abstract class AbstractApp implements OnDestroy, OnInit {

    private dialogRef: MatDialogRef<IScreen>;

    private previousScreenType: string;

    private previousDialogType: string;

    private dialogOpening: boolean;

    private previousScreenName: string;

    private snackBarRef: MatSnackBarRef<SimpleSnackBar>;

    private personalized: boolean;

    private registered: boolean;

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
                setTimeout(() => this.openDialog(), 0);
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

        let template: AbstractTemplate = null;
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
            template = viewContainerRef.createComponent(templateComponentFactory).instance as AbstractTemplate;
            this.previousScreenType = screenType;
            this.previousScreenName = screenName;
            this.overlayContainer.getContainerElement().classList.add(this.getTheme());
            const installedScreen = template.installScreen(this.screenService.resolveScreen(screenType), this.session, this);
            template.show(this.session, this);
            installedScreen.show(this.session.screen, this);
        }

    }

    openDialog() {
        let dialogComponentFactory: ComponentFactory<IScreen>;
        let dialogComponent = DialogComponent;
        this.previousDialogType = 'Dialog';

        let dialogTemplateChild: IScreen = null;

        if (this.session.dialog && this.session.dialog.template === 'Dialog') {
            const viewContainerRef = this.host.viewContainerRef;
            // Resolve the Dialog template
            // Confused on what to do here: I think I really want to create the dialog template as a child of the
            // mat dialog 'view' and not as a child of the host viewContainerRef.  So how do I get the viewContainerRef
            // of the mat dialog??
            const templateComponentFactory: ComponentFactory<IScreen> = this.screenService.resolveScreen('Dialog');
            const dialogTemplate = viewContainerRef.createComponent(templateComponentFactory).instance as DialogTemplateComponent;
            // Install the screen into the template
            const screenType = this.session.dialog.type;
            dialogTemplateChild = dialogTemplate.installScreen(this.screenService.resolveScreen(screenType), this.session, this);
            dialogTemplate.dialogChild = dialogTemplateChild;
            dialogComponent = templateComponentFactory.componentType;
            this.previousDialogType = screenType;
        } else {
            dialogComponentFactory = this.screenService.resolveScreen(this.session.dialog.subType);
        }

        // if we resolved a specific screen type use that otherwise just use the dialog template or the default DialogComponent
        if (dialogComponentFactory) {
            dialogComponent = dialogComponentFactory.componentType;
            this.previousDialogType = this.session.dialog.subType;
        }

        this.dialogRef = this.dialog.open(dialogComponent, { disableClose: true });

        this.dialogRef.componentInstance.show(this.dialog, this);
        if (dialogTemplateChild) {
            dialogTemplateChild.show(this.session.dialog, this);
        }
        this.dialogOpening = false;
        console.log('Dialog \'' + this.previousDialogType + '\' opened');
        this.dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.session.onAction(result);
                this.dialogRef = null;
            }
        });

    }
}
