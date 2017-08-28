import { DialogComponent } from '../screens/dialog.component';
import { IMenuItem } from '../screens/imenuitem';
import { Component, OnInit, OnDestroy, DoCheck } from '@angular/core';
import { SessionService } from '../session.service';
import { StatusBarComponent } from '../screens/statusbar.component';
import { FocusDirective } from '../screens/focus';
import { MdDialog, MdDialogRef } from '@angular/material';

export abstract class AbstractApp implements OnInit, OnDestroy, DoCheck {

    private dialogRef: MdDialogRef<DialogComponent>;

    constructor(public session: SessionService, public dialog: MdDialog) {
    }

    protected abstract appName(): String;

    ngOnInit(): void {
        this.session.unsubscribe();
        this.session.subscribe(this.appName());
    }

    ngOnDestroy(): void {
        this.session.unsubscribe();
    }

    ngDoCheck(): void {
        if (this.session.dialog && !this.dialogRef) {
            setTimeout(() => this.openDialog(), 0);
        }
    }

    openDialog() {
        this.dialogRef = this.dialog.open(DialogComponent, { disableClose: true });
        this.dialogRef.afterClosed().subscribe(result => {
            this.session.onAction(result);
            this.dialogRef = null;
        });
    }
}
