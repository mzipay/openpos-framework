import {IActionItem} from "../../../core/actions/action-item.interface";

export interface Reward {
    promotionId: string;
    name: string;
    expirationDate: string;
    expirationLabel: string;
    applyButton: IActionItem;
};

export interface RewardsLineItemComponentInterface {

}