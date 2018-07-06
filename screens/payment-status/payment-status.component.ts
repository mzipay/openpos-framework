import {Component } from '@angular/core';
import { IMenuItem } from '../../common/imenuitem';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
  selector: 'app-payment-status',
  templateUrl: './payment-status.component.html'
})
export class PaymentStatusComponent extends PosScreen<any> {

  screen: any;
  pinPadStatus = '0';

  constructor() {
      super();
  }

  buildScreen() {
    this.pinPadStatus = this.screen.pinPadStatus;
  }

  public doMenuItemAction(menuItem: IMenuItem) {
    this.session.onAction(menuItem.action, null, menuItem.confirmationMessage );
  }

}
