import { IAbstractScreen, IActionItem, ISellItem } from '../../core';
import { ITotal } from '../../core/interfaces/total.interface';

export interface SaleInterface extends IAbstractScreen {
    transactionMenuPrompt: string;
    transactionMenuItems: IActionItem[];
    multiSelectedMenuItems: IActionItem[];
    localMenuItems: IActionItem[];
    items: ISellItem[];
    selectedItemIndexes: number[];
    totals: ITotal[];
    readOnly: boolean;
    grandTotal: string;
    prompt: string;
    hideCustomer: boolean;
    transactionActive: boolean;
    checkoutButtonText: string;
}
