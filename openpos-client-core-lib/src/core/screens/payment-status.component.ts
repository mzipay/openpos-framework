import { IScreen } from '../common/iscreen';
import {Component, ViewChild, AfterViewInit, DoCheck} from '@angular/core';
import {SessionService} from '../services/session.service';
import { AbstractApp } from '../common/abstract-app';
import { IMenuItem } from '../common/imenuitem';

@Component({
  selector: 'app-payment-status',
  templateUrl: './payment-status.component.html'
})
export class PaymentStatusComponent implements AfterViewInit, DoCheck, IScreen {

  pinPadStatus: string = "0";

  constructor(public session: SessionService) {
  }

  show(session: SessionService, app: AbstractApp) {
  }

  ngDoCheck(): void {
    this.ngOnInit();
  }

  ngAfterViewInit(): void {
    // Poll for status change if servers upports payment status updates.
    this.session.onAction('GetPaymentStatus');
  }

  ngOnInit(): void {
    this.pinPadStatus = this.session.screen.pinPadStatus;
  }

  public doMenuItemAction(menuItem: IMenuItem) {
    this.session.onAction(menuItem.action);
  }

}