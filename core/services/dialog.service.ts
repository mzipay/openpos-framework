import { Injectable, Type, ComponentFactoryResolver, ComponentFactory } from '@angular/core';
import { IScreen } from '../components';
import { SessionService } from './session.service';
import { filter, map } from 'rxjs/operators';
import { BehaviorSubject, Subscription } from 'rxjs';
import { MatDialogRef, MatDialog } from '@angular/material';
import { OpenPOSDialogConfig } from '../interfaces';
@Injectable({
    providedIn: 'root',
  })
export class DialogService {

    private dialogs = new Map<string, Type<IScreen>>();

    private dialogRef: MatDialogRef<IScreen>;

    private dialogOpening: boolean;

    private lastDialogType: string;

    constructor(private componentFactoryResolver: ComponentFactoryResolver, public session: SessionService, public dialog: MatDialog ) {

        // Get just the messages we care about for managing dialogs
        const $dialogMessages = session.getMessages('Screen', 'ClearDialog');

        // Pipe all the messages for dialog updates
        $dialogMessages.pipe(
                    filter( m => (m.template && m.template.dialog) )
                )
            .subscribe( m => this.updateDialog(m) );

        // We want to close the dialog if we get a clear dialog message or its a screen message that isn't a dialog
        $dialogMessages.pipe(
                filter( m => m.clearDialog || (m.template && !m.template.dialog))
            )
        .subscribe( m => this.closeDialog() );


        this.updateDialog({ screenType: 'Startup', template: { type: 'Blank', dialog: true, dialogProperties: { width: '60%' } }});
    }


    public addDialog(name: string, type: Type<IScreen>): void {
        if (this.dialogs.get(name)) {
        // tslint:disable-next-line:max-line-length
        console.log(`replacing registration for screen of type ${this.dialogs.get(name).name} with ${type.name} for the key of ${name} in the screen service`);
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
            console.error(`Could not fine a dialog type of: ${type}.  Please register it with the screen service`);
            return this.componentFactoryResolver.resolveComponentFactory(this.dialogs.get('Blank'));
        }
    }

    public closeDialog() {
        if (this.dialogRef) {
            console.log('closing dialog ref');
            this.dialogRef.close();
            this.dialogRef = null;
        }
    }

    public isDialogOpen(): boolean {
        return this.dialogRef !== null;
    }

    private updateDialog(dialog?: any): void {
        if (dialog) {
            const dialogType = this.hasDialog(dialog.subType) ? dialog.subType : 'Dialog';
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
        }
    }

    private openDialog(dialog: any) {
        const dialogComponentFactory: ComponentFactory<IScreen> = this.resolveDialog(dialog.screenType);
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

        this.dialogRef.componentInstance.show(dialog);
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
}
