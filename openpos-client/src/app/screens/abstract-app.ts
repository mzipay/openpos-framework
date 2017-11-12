import { IconService } from './../icon.service';
import { DomSanitizer } from '@angular/platform-browser';
import { ScreenDirective } from '../common/screen.directive';
import { IScreen } from '../common/iscreen';
import { ScreenService } from './../screen.service';
import { DialogComponent } from '../screens/dialog.component';
import { IMenuItem } from '../common/imenuitem';
import { Component, OnInit, OnDestroy, DoCheck } from '@angular/core';
import { Type, ViewChild, ComponentFactory } from '@angular/core';
import { SessionService } from '../session.service';
import { StatusBarComponent } from '../screens/statusbar.component';
import { FocusDirective } from '../common/focus.directive';
import { Observable } from 'rxjs/Observable';
import { MatDialog, MatDialogRef, MatIconRegistry, MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material';

export abstract class AbstractApp implements OnInit, OnDestroy, DoCheck {

    private dialogRef: MatDialogRef<DialogComponent>;

    private previousScreenType: string;

    private previousScreenSequenceNumber: number;

    private snackBarRef: MatSnackBarRef<SimpleSnackBar>;

    @ViewChild(ScreenDirective) host: ScreenDirective;

    constructor(public screenService: ScreenService,
        public session: SessionService,
        public dialog: MatDialog,
        public iconService: IconService,
        public snackBar: MatSnackBar) {
    }

    protected abstract appName(): String;

    ngOnInit(): void {
        this.session.unsubscribe();
        this.session.subscribe(this.appName());
        this.iconService.registerLocalSvgIcons();
        const timer = Observable.timer(1000, 1000);
        timer.subscribe(t => this.checkConnectionStatus(this.session));
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
        if (this.session.screen &&
            ((this.session.screen.sequenceNumber !== this.previousScreenSequenceNumber && this.session.screen.refreshAlways)
                || this.session.screen.type !== this.previousScreenType)) {
            console.log(`Switching screens from ${this.previousScreenType} to ${this.session.screen.type}`);
            const componentFactory: ComponentFactory<IScreen> = this.screenService.resolveScreen(this.session.screen.type);
            const viewContainerRef = this.host.viewContainerRef;
            viewContainerRef.clear();
            screen = viewContainerRef.createComponent(componentFactory).instance;
            this.previousScreenType = this.session.screen.type;
            screen.show(this.session);
            this.previousScreenSequenceNumber = this.session.screen.sequenceNumber;
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
