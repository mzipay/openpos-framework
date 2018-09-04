import { Injectable, Type, ComponentFactoryResolver, ComponentFactory } from '@angular/core';
import { IScreen } from '../components';
import { SessionService } from './session.service';
import { filter } from 'rxjs/operators';
import { BehaviorSubject } from 'rxjs';
import { MatDialogRef, MatDialog } from '@angular/material';
import { OpenPOSDialogConfig } from '../interfaces';
import { StartupService, StartupStatus } from './startup.service';
@Injectable({
    providedIn: 'root',
  })
export class DialogService {

    private dialogs = new Map<string, Type<IScreen>>();

    private dialogRef: MatDialogRef<IScreen>;

    private dialogOpening: boolean;

    private lastDialogType: string;

    // Used for notifying when current dialog has closed so that, in case of consec. dialogs,
    // the next dialog won't be opened until current one is closed.
    private dialogClosed = new BehaviorSubject<boolean>(true);

    constructor(
        private componentFactoryResolver: ComponentFactoryResolver,
        private session: SessionService,
        private dialog: MatDialog,
        private startupService: StartupService) {


        // Get just the messages we care about for managing dialogs
        const $dialogMessages = session.getMessages('Screen', 'ClearDialog');

        this.startupService.onStartupCompleted.subscribe(startupStatus => {
            if (startupStatus === StartupStatus.Success || startupStatus === StartupStatus.Failure) {
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

            }
        });

        this.updateDialog({ screenType: 'Startup', template: { type: 'Blank', dialog: true, dialogProperties: { width: '60%', panelClass: 'startup-dialog-container' } }});
        this.startupService.onStartupCompleted.subscribe(startupStatus => {
            if (startupStatus === StartupStatus.Success) {
                // Ensure the startup dialog is closed if we have a successful startup
                if (this.isDialogOpen('Startup')) {
                    this.closeDialog();
                }
            }
        });
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
            console.error(`Could not find a dialog type of: ${type}.  Please register it with the screen service`);
            return this.componentFactoryResolver.resolveComponentFactory(this.dialogs.get('Blank'));
        }
    }

    public closeDialog() {
        if (this.dialogRef) {
            console.log('[DialogService] closing dialog ref');
            this.dialogRef.close();
            this.dialogRef = null;
        }
    }

    /** screenType - If given, behavior will only report true if there is a dialog open AND it's of the given type */
    public isDialogOpen(screenType?: string): boolean {
        if (screenType) {
            return screenType === this.lastDialogType && this.dialogRef !== null;
        }

        return this.dialogRef !== null;
    }

    private updateDialog(dialog?: any): void {
        if (dialog) {
            const dialogType = this.hasDialog(dialog.subType) ? dialog.subType : 'Dialog';
            if (!this.dialogOpening) {
                // console.log(`[DialogService] dialogRef=${this.dialogRef}, dialog.screenType=${dialog.screenType}, this.lastDialogType=${this.lastDialogType}`);
                this.closeDialog();
                // Don't open next dialog until current one has closed.
                this.dialogClosed.subscribe(complete => {
                    if (complete) {
                        console.log('[DialogService] opening dialog \'' + dialogType + '\'');
                        this.dialogOpening = true;
                        setTimeout(() => this.openDialog(dialog), 0);
                    }
                });
            } else {
                console.log(`[DialogService] Not opening dialog! Here's why: dialogOpening? ${this.dialogOpening}`);
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

        if (this.dialogClosed) {
            this.dialogClosed.unsubscribe();
        }

        if (!this.dialogRef || dialog.screenType !== this.lastDialogType || dialog.screenType === 'Dialog'
            || dialog.refreshAlways) {
            this.dialogRef = this.dialog.open(dialogComponent, dialogProperties);
        } else {
            console.log(`Using previously created dialogRef. current dialog type: ${dialog.screenType}, last dialog type: ${this.lastDialogType}`);
        }

        this.session.loaderState.loading = false;

        this.dialogRef.componentInstance.show(dialog);
        this.dialogOpening = false;
        this.dialogClosed = new BehaviorSubject<boolean>(false);
        console.log('[DialogService] Dialog \'' + dialog.screenType + '\' opened');
        if (dialogProperties.executeActionBeforeClose) {
            this.handleActionBeforeClose(closeAction);
        } else {
            this.handleActionAfterClose(closeAction);
        }

        this.lastDialogType = dialog.screenType;
    }

    private handleActionBeforeClose(closeAction: any) {
        console.log(`Using 'BeforeClose' logic`);
        // Some dialogs may need to execute the chosen action before
        // they close so that actionPayloads can be included with the action
        // before the dialog is destroyed.
        this.dialogRef.beforeClose().subscribe(result => {
            const action = closeAction || result;
            this.session.onAction(action);
            // Block input while closing
            if (action) {
                this.session.loaderState.loading = true;
            }
            this.dialogClosed.next(true);
        });
    }

    private handleActionAfterClose(closeAction: any) {
        // Temp hack to stop user actions while the dialog is closing
        console.log(`Using 'AfterClose' logic`);
        // Disable actions while the dialog is closing
        this.dialogRef.beforeClose().subscribe(result => {
            const action = closeAction || result;
            // Disable input while we are closing if there is an action for this dialog to be returned
            if (action) {
                this.session.loaderState.loading = true;
            }
        });
        this.dialogRef.afterClosed().subscribe(result => {
            const action = closeAction || result;
            // Need to temporarily allow input again
            this.session.loaderState.loading = false;
            // Send the action through
            this.session.onAction(action);
            // Block input again
            if (action) {
                this.session.loaderState.loading = true;
            }
            this.dialogClosed.next(true);
        });
    }
}
