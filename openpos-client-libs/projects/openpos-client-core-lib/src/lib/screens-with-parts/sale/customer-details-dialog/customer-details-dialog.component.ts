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
  buildScreen() {
    this.screen.rewards = [];
  }

  getRewardsLabel() : string {
    return this.screen.rewardsLabel + ((this.screen.rewards) ? ' (' + this.screen.rewards.length + ')': '');
  }
}
