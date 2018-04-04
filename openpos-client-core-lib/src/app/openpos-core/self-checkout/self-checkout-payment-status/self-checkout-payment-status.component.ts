import { Component, OnInit } from '@angular/core';

import { SessionService } from '../../services/session.service';
import { AbstractApp } from '../../common/abstract-app';

@Component({
  selector: 'app-self-checkout-payment-status-component',
  templateUrl: './self-checkout-payment-status.component.html'
})
export class SelfCheckoutPaymentStatusComponent implements OnInit {

  screen: any;
  balanceDue: string = "0.00";
  instructions: string = "";
  additionalInstructions: string = "";

  constructor(public session: SessionService) { }

  show(screen: any, app: AbstractApp) {
    this.screen = screen;

    this.balanceDue = this.screen.balanceDue;
    this.instructions = this.screen.instructions;
    if (this.screen.additionalInstructions) {
      this.additionalInstructions = this.screen.additionalInstructions;
    }
    
  }

  ngOnInit() {
  }
}