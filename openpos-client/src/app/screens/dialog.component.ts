import { IMenuItem } from '../common/imenuitem';
import { Component } from '@angular/core';
import { SessionService } from '../session.service';
import { FocusDirective } from '../common/focus.directive';
import { MatDialog, MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html'
})
export class DialogComponent {

  constructor(public session: SessionService, public dialogRef: MatDialogRef<DialogComponent>) {
  }

}
