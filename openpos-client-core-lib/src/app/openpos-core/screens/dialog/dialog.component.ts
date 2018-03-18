import { IMenuItem } from '../../common/imenuitem';
import { Component } from '@angular/core';
import { SessionService } from '../../services/session.service';
import { FocusDirective } from '../../common/focus.directive';
import { MatDialog, MatDialogRef } from '@angular/material';
import { IScreen } from '../../common/iscreen';
import { AbstractApp } from '../../index';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html'
})
export class DialogComponent implements IScreen {


  constructor(public session: SessionService, public dialogRef: MatDialogRef<DialogComponent>) {
  }

  show(screen: any, app: AbstractApp): void {

  }

}
