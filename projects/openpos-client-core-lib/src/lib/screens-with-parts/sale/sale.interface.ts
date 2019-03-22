import { IAbstractScreen, IActionItem } from '../../core';
import { ITotal } from '../../core/interfaces/total.interface';

export interface SaleInterface extends IAbstractScreen {
    totals: ITotal[];
    transactionActive: boolean;
    customerName: string;
    noCustomerText: string;
    loyaltyButton: IActionItem;
    checkoutButton: IActionItem;
}
