import { IconService } from './../services/icon.service';
import { DomSanitizer } from '@angular/platform-browser';
import { ScreenDirective } from '../common/screen.directive';
import { IScreen } from '../common/iscreen';
import { ScreenService } from './../services/screen.service';
import { DialogComponent } from '../screens/dialog.component';
import { IMenuItem } from '../common/imenuitem';
import { Component, OnInit, OnDestroy, DoCheck, sequence } from '@angular/core';
import { Type, ViewChild, ComponentFactory } from '@angular/core';
import { SessionService } from '../services/session.service';
import { StatusBarComponent } from '../screens/statusbar.component';
import { FocusDirective } from '../common/focus.directive';
import { Observable } from 'rxjs/Observable';
import { MatDialog, MatDialogRef, MatIconRegistry, MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material';
import { OverlayContainer } from '@angular/cdk/overlay';
import { TemplateDirective } from './template.directive';
import { AbstractTemplate } from './abstract-template';

export abstract class AbstractApp implements OnInit, OnDestroy, DoCheck {

    private dialogRef: MatDialogRef<DialogComponent>;

    private previousScreenType: string;

    private previousScreenSequenceNumber: number;

    private previousScreenName: string;

    private snackBarRef: MatSnackBarRef<SimpleSnackBar>;

    private needsPersonalization: boolean;

    @ViewChild(TemplateDirective) host: TemplateDirective;

    constructor(public screenService: ScreenService,
        public session: SessionService,
        public dialog: MatDialog,
        public iconService: IconService,
        public snackBar: MatSnackBar,
        public overlayContainer: OverlayContainer) {
    }

    public abstract appName(): string;

    ngOnInit(): void {
        this.needsPersonalization = !this.session.isPersonalized();
        if (!this.needsPersonalization) {
            this.initializeApplication();
        }
    }

    public initializeApplication(): void {
        this.session.unsubscribe();
        this.session.subscribe(this.appName());
        this.iconService.registerLocalSvgIcons();
        const timer = Observable.timer(1000, 1000);
        timer.subscribe(t => this.checkConnectionStatus(this.session));
    }

    public getTheme(): string {
        if (this.session.screen && this.session.screen.theme) {
            localStorage.setItem('theme', this.session.screen.theme);
            return this.session.screen.theme;
        } else if (localStorage.getItem('theme')) {
            return localStorage.getItem('theme');
        } else {
            return 'openpos-theme';
        }
    }

    protected checkConnectionStatus(session: SessionService): void {
        const connected = session.connected();
        if (!connected && !this.snackBarRef) {
            this.snackBarRef = this.snackBar.open('The server is disconnected', undefined, {
                duration: 0, viewContainerRef: null, verticalPosition: 'top'
            });
            this.snackBarRef.afterDismissed().subscribe(() => {
                this.snackBarRef = null;
            });
        } else if (connected) {
            this.snackBar.dismiss();
            this.snackBarRef = null;
        }
    }

    ngOnDestroy(): void {
        this.session.unsubscribe();
    }

    ngDoCheck(): void {
        if (this.session.dialog && !this.dialogRef) {
            setTimeout(() => this.openDialog(), 0);
        } else if (!this.session.dialog && this.dialogRef) {
            console.log('closing dialog');
            this.dialogRef.close();
            this.dialogRef = null;
        }

        let screen: IScreen = null;
        let template: AbstractTemplate = null;
        if (this.needsPersonalization ||
            (this.session.screen &&
                ((this.session.screen.sequenceNumber !== this.previousScreenSequenceNumber && this.session.screen.refreshAlways)
                    || this.session.screen.type !== this.previousScreenType
                    || this.session.screen.name !== this.previousScreenName
                )
            )) {

            let templateName: string = null;
            let screenType: string = null;
            let sequenceNumber: number = -1;
            let screenName: string = null;
            if (this.session.screen && this.session.screen.type) {
                console.log(`Switching screens from ${this.previousScreenType} to ${this.session.screen.type}`);
                templateName = this.session.screen.template;
                screenType = this.session.screen.type;
                sequenceNumber = this.session.screen.sequenceNumber;
                screenName = this.session.screen.name;
            } else {
                templateName = 'Blank';
                screenType = 'Personalization';
            }
            const templateComponentFactory: ComponentFactory<IScreen> = this.screenService.resolveScreen(templateName);
            const viewContainerRef = this.host.viewContainerRef;
            viewContainerRef.clear();
            template = viewContainerRef.createComponent(templateComponentFactory).instance as AbstractTemplate;
            this.previousScreenType = screenType;
            this.previousScreenName = screenName;
            this.overlayContainer.getContainerElement().classList.add(this.getTheme());
            screen = template.installScreen(this.screenService.resolveScreen(screenType), this.session, this);
            template.show(this.session, this);
            screen.show(this.session, this);
            this.previousScreenSequenceNumber = sequenceNumber;
            this.needsPersonalization = false;
        }

    }

    openDialog() {
        this.dialogRef = this.dialog.open(DialogComponent, { disableClose: true });
        this.dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.session.onAction(result);
                this.dialogRef = null;
            }
        });
    }
}
