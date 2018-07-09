
import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { IMenuItem } from '../../core';
import { ILine } from './line.interface';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: [ './dialog.component.scss']
})
export class DialogComponent extends PosScreen<any> {

  primaryButton: IMenuItem;
  otherButtons: IMenuItem[];
  messages: string[];
  lines: ILine[];

  constructor( public dialogRef: MatDialogRef<DialogComponent> ) {
      super();
  }

  buildScreen(): void {
    this.messages = this.screen.message;
    this.lines = this.screen.messageLines;
 
    if( this.screen.buttons ){
      this.primaryButton = this.screen.buttons[0];
    }

    if( this.screen.buttons && this.screen.buttons.length > 1 ){
      this.otherButtons = this.screen.buttons.slice(1, this.screen.buttons.length);
    }
  }

  isCustomerFacing(): boolean {
    return this.screen.customerFacing && ['CUSTOMER_CONFIRMATION'].indexOf(this.screen.subType) >= 0;
  }
}
