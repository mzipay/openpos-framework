import { Logger } from './logger.service';

import { Injectable, Type, ComponentFactoryResolver, ComponentFactory } from '@angular/core';
import { SessionService } from './session.service';
import { filter } from 'rxjs/operators';
import { IScreen } from '../components/dynamic-screen/screen.interface';
import { DialogContentComponent } from '../components/dialog-content/dialog-content.component';
import { MatDialogRef, MatDialog } from '@angular/material';
import { OpenPOSDialogConfig } from '../interfaces';
import { Observable, BehaviorSubject } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class DialogService {

    private dialogs = new Map<string, Type<IScreen>>();

    private dialogRef: MatDialogRef<DialogContentComponent>;

    private dialogOpening: boolean;

    private lastDialogType: string;

    private $dialogMessages = new BehaviorSubject<any>(null);

    constructor(
        private log: Logger,
        private componentFactoryResolver: ComponentFactoryResolver,
        private session: SessionService,
        private dialog: MatDialog) {

        // Use BehaviorSubject to hang on to most recent dialog so it can be displayed
        // once dialog service has started. Handles case where server is 'showing' a dialog
        // but client is starting up. If we wait to subscribe until start() method, we can
        // miss the dialog.
        this.session.getMessages('Dialog').subscribe(s => { if (s) { this.$dialogMessages.next(s); } } );
    }

    public start() {
        // Defer set up of dialog subscriptions for updating dialogs until service has been started.
        // Addresses problem of dialogs from server side being shown before app startup has finished.
        // We use a Startup Task to invoke this start method at nearly the end of startup.

        // Pipe all the messages for dialog updates
        this.$dialogMessages.subscribe(m => this.updateDialog(m));

    }

    public addDialog(name: string, type: Type<IScreen>): void {
        if (this.dialogs.get(name)) {
            // tslint:disable-next-line:max-line-length
            this.log.info(`replacing registration for screen of type ${this.dialogs.get(name).name} with ${type.name} for the key of ${name} in the screen service`);
            this.dialogs.delete(name);
        }
        this.dialogs.set(name, type);
    }

    public hasDialog(name: string): boolean {
        return this.dialogs.has(name);
    }

    private resolveDialog(type: string): ComponentFactory<IScreen> {
        const dialogType: Type<IScreen> = this.dialogs.get(type);
        if (dialogType) {
            return this.componentFactoryResolver.resolveComponentFactory(dialogType);
        } else {
            console.error(`Could not find a dialog type of: ${type}.  Please register it with the screen service`);
            return this.componentFactoryResolver.resolveComponentFactory(this.dialogs.get('Blank'));
        }
    }

    public closeDialog(cancelLoading: boolean) {
        if (this.dialogRef) {
            this.log.info('[DialogService] closing dialog ref');
            if (cancelLoading) {
              this.dialogRef.afterClosed().subscribe(result => {
                  this.session.cancelLoading();
              });
            }
            this.dialogRef.close();
            this.dialogRef = null;
        } else if (cancelLoading) {
            this.session.cancelLoading();
        }
    }

    /** screenType - If given, behavior will only report true if there is a dialog open AND it's of the given type */
    public isDialogOpen(screenType?: string): boolean {
        if (screenType) {
            return screenType === this.lastDialogType && this.dialogRef !== null;
        }

        return this.dialogRef && this.dialogRef !== null;
    }

    private updateDialog(dialog?: any): void {
        if (dialog) {
            const dialogType = this.hasDialog(dialog.subType) ? dialog.subType : 'Dialog';
            if (!this.dialogOpening) {
                this.log.info('opening dialog \'' + dialogType + '\'');
                this.dialogOpening = true;
                setTimeout(() => this.openDialog(dialog), 0);
            } else {
                this.log.info(`[DialogService] Not opening dialog! Here's why: dialogOpening? ${this.dialogOpening}`);
            }
        }
    }

    private openDialog(dialog: any) {
        const dialogComponentFactory: ComponentFactory<IScreen> = this.resolveDialog(dialog.screenType);
        let closeable = false;
        let forceReopen = false;
        if (dialog.dialogProperties) {
            closeable = dialog.dialogProperties.closeable;
            forceReopen = dialog.dialogProperties.forceReopen;
        }
        // By default we want to not allow the user to close by clicking off
        // By default we need the dialog to grab focus so you cannont execute actions on the screen
        // behind by hitting enter
        const dialogProperties: OpenPOSDialogConfig = { disableClose: !closeable, autoFocus: true };
        // const dialogComponent = dialogComponentFactory.componentType;
        if (dialog.dialogProperties) {
            // Merge in any dialog properties provided on the screen
            for (const key in dialog.dialogProperties) {
                if (dialog.dialogProperties.hasOwnProperty(key)) {
                    dialogProperties[key] = dialog.dialogProperties[key];
                }
            }
            this.log.info(`Dialog options: ${JSON.stringify(dialogProperties)}`);
        }

        if (!this.dialogRef || dialog.screenType !== this.lastDialogType || dialog.screenType === 'Dialog'
            || dialog.refreshAlways) {

            if (forceReopen) {
                this.closeDialog(false);
            }

            if (!this.dialogRef || !this.dialogRef.componentInstance) {
                this.log.info('[DialogService] Dialog \'' + dialog.screenType + '\' opening...');
                this.dialogRef = this.dialog.open(DialogContentComponent, dialogProperties);
            } else {
                this.log.info('[DialogService] Dialog \'' + dialog.screenType + '\' refreshing content...');
                this.dialogRef.updateSize('' + dialogProperties.minWidth, '' + dialogProperties.minHeight);
                this.dialogRef.disableClose = dialogProperties.disableClose;
            }
            this.dialogRef.componentInstance.installScreen(dialogComponentFactory);
            this.session.cancelLoading();
        } else {
            this.log.info(`Using previously created dialogRef. current dialog type: ${dialog.screenType}, last dialog type: ${this.lastDialogType}`);
        }

        this.log.info('[DialogService] Dialog \'' + dialog.screenType + '\' showing...');
        this.dialogRef.componentInstance.show(dialog);
        this.log.info('[DialogService] Dialog \'' + dialog.screenType + '\' opened/shown');
        this.dialogOpening = false;

        this.lastDialogType = dialog.screenType;
    }

}
