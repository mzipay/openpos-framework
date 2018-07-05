import {Component } from '@angular/core';
import { IScreen } from '../../common/iscreen';
import { SessionService } from '../../services/session.service';
import { IMenuItem } from '../../common/imenuitem';

@Component({
  selector: 'app-payment-status',
  templateUrl: './payment-status.component.html'
})
export class PaymentStatusComponent implements IScreen {

  screen: any;
  pinPadStatus = '0';

  constructor(public session: SessionService) {
  }

  show(screen: any) {
    this.screen = screen;
    this.pinPadStatus = this.screen.pinPadStatus;
  }

  public doMenuItemAction(menuItem: IMenuItem) {
    this.session.onAction(menuItem.action, null, menuItem.confirmationMessage );
  }

}
