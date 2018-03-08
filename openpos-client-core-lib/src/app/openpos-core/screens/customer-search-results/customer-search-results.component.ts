import { Component, OnInit} from '@angular/core';

import { IScreen } from '../../common/iscreen';
import { SessionService } from '../../services/session.service';
import { AbstractApp } from '../../common/abstract-app';
import { ICustomer } from '../../common/icustomer';

@Component({
  selector: 'app-customer-search-results',
  templateUrl: './customer-search-results.component.html',
  styleUrls: ['./customer-search-results.component.scss']
})
export class CustomerSearchResultsComponent implements IScreen, OnInit {

  public customers: ICustomer[];

  constructor(public session: SessionService) { }

  show(session: SessionService, app: AbstractApp) {
  }

  ngOnInit() {   
    this.customers = this.session.screen.customers;
  }
}