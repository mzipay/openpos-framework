import {Component } from '@angular/core';
import { PosScreen } from '../pos-screen/pos-screen.component';
import { IActionItem } from '../../core/interfaces/menu-item.interface';

/**
 * @ignore
 */
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

  public doMenuItemAction(menuItem: IActionItem) {
    this.session.onAction(menuItem);
  }

}
