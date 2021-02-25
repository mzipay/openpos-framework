import {IAbstractScreen} from "../../core/interfaces/abstract-screen.interface";
import {IActionItem} from "../../core/actions/action-item.interface";

export interface CustomerSearchResultDialogInterface extends IAbstractScreen {
    instructions: String;
    selectButton: IActionItem;
    viewButton: IActionItem;
    results: ICustomerDetails[]
}

export interface ICustomerDetails {
    name: string,
    loyaltyNumber: string,
    phoneNumber: string,
    email: string,
    address: {line1: string, line2: string, city: string, state: string, postalCode: string}
}