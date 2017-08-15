import { IMenuItem } from './imenuitem';
import { Component } from '@angular/core';
import { SessionService } from '../session.service';
import { FocusDirective } from './focus';
import { MdDialog, MdDialogRef } from '@angular/material';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html'
})
export class DialogComponent {

  constructor(public session: SessionService, public dialogRef: MdDialogRef<DialogComponent>) {
  }

}
