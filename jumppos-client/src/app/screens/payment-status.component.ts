import { IScreen } from './iscreen';
import {Component, ViewChild, AfterViewInit, DoCheck} from '@angular/core';
import {SessionService} from '../session.service';

@Component({
  selector: 'app-payment-status',
  templateUrl: './payment-status.component.html'
})
export class PaymentStatusComponent implements AfterViewInit, DoCheck, IScreen {

  constructor(public session: SessionService) {
  }

  show(session: SessionService) {
  }

  ngDoCheck(): void {
  }

  ngAfterViewInit(): void {
  }

}
