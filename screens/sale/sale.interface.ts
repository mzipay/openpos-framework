import { IAbstractScreen, IMenuItem, ISellItem } from '../../core';
import { ITotal } from '../../core/interfaces/total.interface';

export interface SaleInterface extends IAbstractScreen {
    transactionMenuPrompt: string;
    transactionMenuItems: IMenuItem[];
    multiSelectedMenuItems: IMenuItem[];
    localMenuItems: IMenuItem[];
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
