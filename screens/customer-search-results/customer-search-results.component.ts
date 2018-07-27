import { Component, Input } from '@angular/core';

import { ICustomer } from './customer.interface';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
  selector: 'app-customer-search-results',
  templateUrl: './customer-search-results.component.html',
  styleUrls: ['./customer-search-results.component.scss']
})
export class CustomerSearchResultsComponent extends PosScreen<any> {

  @Input() submitAction: string;
  public customers: ICustomer[];
  selectedOptions: ICustomer[];

  buildScreen() {
    this.customers = this.screen.customers;
    this.submitAction = this.screen.submitAction;
  }

  onSubmitAction(): void {
    this.session.onAction(this.submitAction, this.selectedOptions);
  }

  isSelectedOptionsEmpty(): boolean {
    return Boolean(typeof this.selectedOptions === 'undefined' || this.selectedOptions.length === 0);
  }

}
