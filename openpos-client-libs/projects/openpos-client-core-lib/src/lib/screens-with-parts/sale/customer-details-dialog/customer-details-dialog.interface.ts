import {IAbstractScreen} from "../../../core/interfaces/abstract-screen.interface";
import {IActionItem} from "../../../core/actions/action-item.interface";
import {Membership} from "../../../shared/screen-parts/membership-display/memebership-display.interface";

export interface Reward {
    promotionId: string;
    name: string;
    expirationDate: string;
    expirationLabel: string;
    applyButton: IActionItem;
};

export interface CustomerDetailsDialogInterface extends IAbstractScreen {
    message: string;
    customer: {
        name: string,
        loyaltyNumber: string,
        phoneNumber: string,
        email: string,
        address: {
            line1: string,
            line2: string,
            city: string,
            state: string,
            postalCode: string
        },
        memberships: Membership[],
        rewards: Reward[]
    };
    membershipEnabled: boolean;
    membershipLabel: String;
    loyaltyPromotions: IActionItem;
    editButton: IActionItem;
    unlinkButton: IActionItem;
    doneButton: IActionItem;
    rewardsLabel: string;
    rewardHistoryLabel: string;
    noPromotionsText: string;
}
