import {Component, Input} from '@angular/core';
import {Reward} from "../../../screens-with-parts/sale/customer-details-dialog/customer-details-dialog.interface";


@Component({
    selector: 'app-rewards-line-item',
    templateUrl: './rewards-line-item.component.html',
    styleUrls: ['./rewards-line-item.component.scss']})
export class RewardsLineItemComponent {
    @Input()
    reward: Reward;

    @Input()
    householdEnabled: boolean = false;

    constructor() {}
}
