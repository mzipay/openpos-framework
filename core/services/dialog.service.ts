import { Logger } from './logger.service';

import { Injectable, Type, ComponentFactoryResolver, ComponentFactory } from '@angular/core';
import { SessionService } from './session.service';
import { filter } from 'rxjs/operators';
import { IScreen } from '../components/dynamic-screen/screen.interface';
import { DialogContentComponent } from '../components/dialog-content/dialog-content.component';
import { MatDialogRef, MatDialog } from '@angular/material';
import { OpenPOSDialogConfig } from '../interfaces';
import { StartupService } from './startup.service';
@Injectable({
    providedIn: 'root',
})
export class DialogService {

    private dialogs = new Map<string, Type<IScreen>>();

    private dialogRef: MatDialogRef<DialogContentComponent>;

    private dialogOpening: boolean;

    private lastDialogType: string;

    constructor(
        private log: Logger,
        private componentFactoryResolver: ComponentFactoryResolver,
        private session: SessionService,
        private dialog: MatDialog,
        private startupService: StartupService) {


        // Get just the messages we care about for managing dialogs
        const $dialogMessages = session.getMessages('Screen');

        // Pipe all the messages for dialog updates
        $dialogMessages.pipe(
            filter(m => (m.template && m.template.dialog))
        )
            .subscribe(m => this.updateDialog(m));

        // We want to close the dialog if we get a clear dialog message or its a screen message that isn't a dialog
        $dialogMessages.pipe(
            filter(m => m.clearDialog || (m.template && !m.template.dialog))
        )
            .subscribe(m => this.closeDialog());
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

    public closeDialog() {
        if (this.dialogRef) {
            this.log.info('[DialogService] closing dialog ref');
            this.dialogRef.close();
            this.dialogRef = null;
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
        if (dialog.template.dialogProperties) {
            closeable = dialog.template.dialogProperties.closeable;
        }
        // By default we want to not allow the user to close by clicking off
        // By default we need the dialog to grab focus so you cannont execute actions on the screen
        // behind by hitting enter
        const dialogProperties: OpenPOSDialogConfig = { disableClose: !closeable, autoFocus: true };
        // const dialogComponent = dialogComponentFactory.componentType;
        if (dialog.template.dialogProperties) {
            // Merge in any dialog properties provided on the screen
            for (const key in dialog.template.dialogProperties) {
                if (dialog.template.dialogProperties.hasOwnProperty(key)) {
                    dialogProperties[key] = dialog.template.dialogProperties[key];
                }
            }
            this.log.info(`Dialog options: ${JSON.stringify(dialogProperties)}`);
        }

        if (!this.dialogRef || dialog.screenType !== this.lastDialogType || dialog.screenType === 'Dialog'
            || dialog.refreshAlways) {
            if (!this.dialogRef) {
                this.log.info('[DialogService] Dialog \'' + dialog.screenType + '\' opening...');
                this.dialogRef = this.dialog.open(DialogContentComponent, dialogProperties);
                this.dialogRef.afterClosed().subscribe(() => {
                    this.session.cancelLoading();
                });
            } else {
                this.log.info('[DialogService] Dialog \'' + dialog.screenType + '\' refreshing content...');
            }
            this.dialogRef.componentInstance.installScreen(dialogComponentFactory);
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
