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
  primaryButton: IMenuItem;
  otherButtons: IMenuItem[];

  constructor(public session: SessionService, public dialogRef: MatDialogRef<DialogComponent>) {
  }

  show(screen: any): void {
    this.screen = screen;
    
    if( screen.buttons ){
      this.primaryButton = screen.buttons[0];
    }

    if( screen.buttons && screen.buttons.length > 1 ){
      this.otherButtons = screen.buttons.slice(1, screen.buttons.length);
    }
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
