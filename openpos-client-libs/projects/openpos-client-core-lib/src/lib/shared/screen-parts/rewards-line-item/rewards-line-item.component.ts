import {Component, Input} from '@angular/core';
import {Reward} from "../../../screens-with-parts/sale/customer-details-dialog/customer-details-dialog.interface";
import {ScreenPartComponent} from "../screen-part";
import {RewardsLineItemComponentInterface} from "./rewards-line-item.interface";


@Component({
    selector: 'app-rewards-line-item',
    templateUrl: './rewards-line-item.component.html',
    styleUrls: ['./rewards-line-item.component.scss']})
export class RewardsLineItemComponent extends ScreenPartComponent<RewardsLineItemComponentInterface>{
    @Input()
    reward: Reward;

    screenDataUpdated() {
    }
}
