import { IMenuItem } from '../../common/imenuitem';
import { Component, ViewChild, ElementRef } from '@angular/core';
import { SessionService } from '../../services/session.service';
import { FocusDirective } from '../../common/focus.directive';
import { MatDialog, MatDialogRef } from '@angular/material';
import { IScreen } from '../../common/iscreen';
import { ILine } from '../iLine';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: [ './dialog.component.scss']
})
export class DialogComponent implements IScreen {

  screen: any;
  primaryButton: IMenuItem;
  otherButtons: IMenuItem[];
  messages: string[];
  lines: ILine[];

  constructor(public session: SessionService, public dialogRef: MatDialogRef<DialogComponent>) {
  }

  show(screen: any): void {
    this.screen = screen;
    this.messages = this.screen.message;
    this.lines = this.screen.messageLines;
 
    if( screen.buttons ){
      this.primaryButton = screen.buttons[0];
    }

    if( screen.buttons && screen.buttons.length > 1 ){
      this.otherButtons = screen.buttons.slice(1, screen.buttons.length);
    }
  }

  isCustomerFacing(): boolean {
    return this.screen.customerFacing && ['CUSTOMER_CONFIRMATION'].indexOf(this.screen.subType) >= 0;
  }
}
