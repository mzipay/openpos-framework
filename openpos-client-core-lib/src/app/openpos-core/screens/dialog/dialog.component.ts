import { IMenuItem } from '../../common/imenuitem';
import { Component } from '@angular/core';
import { SessionService } from '../../services/session.service';
import { FocusDirective } from '../../common/focus.directive';
import { MatDialog, MatDialogRef } from '@angular/material';
import { IScreen } from '../../common/iscreen';

declare var version: any;

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html'
})
export class DialogComponent implements IScreen {

  screen: any;

  constructor(public session: SessionService, public dialogRef: MatDialogRef<DialogComponent>) {
  }

  show(screen: any): void {
    this.screen = screen;
  }

  get messages(): string[] {
    const targetMessages: Array<string> = new Array;
    const sourceMessages = this.screen.message;
    let clientVersion: string;
    if (typeof version === 'undefined') {
      clientVersion = 'unknown';
    } else {
      clientVersion = version;
    }
    if (sourceMessages) {
      for (let i = 0; i < sourceMessages.length; i++) {
        targetMessages.push(sourceMessages[i].replace('$version$', clientVersion));
      }
    }
    return targetMessages;
  }

}
