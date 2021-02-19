import {IAbstractScreen} from "../../../core/interfaces/abstract-screen.interface";
import {IActionItem} from "../../../core/actions/action-item.interface";

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
        memberships: {
            id: string,
            name: string,
            isMember: boolean
        }
    };
    loyaltyPromotions: IActionItem;
    editButton: IActionItem;
    unlinkButton: IActionItem;
    doneButton: IActionItem;
}
