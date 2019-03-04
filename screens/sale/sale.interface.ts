import { IAbstractScreen, IActionItem, ISellItem } from '../../core';
import { ITotal } from '../../core/interfaces/total.interface';

export interface SaleInterface extends IAbstractScreen {
    transactionMenuPrompt: string;
    transactionMenuItems: IActionItem[];
    multiSelectedMenuItems: IActionItem[];
    items: ISellItem[];
    selectedItemIndexes: number[];
    totals: ITotal[];
    transactionActive: boolean;
    customerName: string;
    noCustomerText: string;
    loyaltyButton: IActionItem;
}
