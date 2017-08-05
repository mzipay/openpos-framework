import {Component, ViewChild, AfterViewInit, DoCheck} from '@angular/core';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {SessionService} from '../session.service';

@Component({
  selector: 'app-payment-status',
  templateUrl: './payment-status.component.html'
})
export class PaymentStatusComponent implements AfterViewInit, DoCheck {

  initialized = false;

  constructor(public session: SessionService) {
  }

  ngDoCheck(): void {
  }

  ngAfterViewInit(): void {
    this.initialized = true;
  }

}
