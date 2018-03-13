import { Component, OnInit, Input} from '@angular/core';

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

  @Input() submitAction: string;
  public customers: ICustomer[];
  selectedOptions: ICustomer[];

  constructor(public session: SessionService) { }

  show(screen: any, app: AbstractApp) {
  }

  ngOnInit() {   
    this.customers = this.session.screen.customers;
    this.submitAction = this.session.screen.submitAction;
  }

  onSubmitAction(): void {
    this.session.onAction(this.submitAction, this.selectedOptions);
  }

  isSelectedOptionsEmpty(): boolean {
    return Boolean(typeof this.selectedOptions === "undefined" || this.selectedOptions.length === 0);
  }

}