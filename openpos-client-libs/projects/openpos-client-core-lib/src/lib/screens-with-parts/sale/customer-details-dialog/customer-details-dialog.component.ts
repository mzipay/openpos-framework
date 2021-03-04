import { Component } from '@angular/core';
import {CustomerDetailsDialogInterface} from "./customer-details-dialog.interface";
import {DialogComponent} from "../../../shared/decorators/dialog-component.decorator";
import {PosScreen} from "../../pos-screen/pos-screen.component";

@DialogComponent({
  name: 'CustomerDetailsDialog'
})
@Component({
  selector: 'app-customer-details-dialog',
  templateUrl: './customer-details-dialog.component.html',
  styleUrls: ['./customer-details-dialog.component.scss']
})
export class CustomerDetailsDialogComponent extends PosScreen<CustomerDetailsDialogInterface> {
  buildScreen() {}

  getRewardsLabel() : string {
    return this.screen.rewardsLabel + ((this.screen.customer.rewards) ? ' (' + this.screen.customer.rewards.length + ')': '');
  }
}
