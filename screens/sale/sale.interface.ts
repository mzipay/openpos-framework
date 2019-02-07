import { IAbstractScreen, IMenuItem, ISellItem } from '../../core';
import { ITotal } from '../../core/interfaces/total.interface';
import { Transaction } from './transaction.interface';

export interface SaleInterface extends IAbstractScreen {
    transactionMenuItems: IMenuItem[];
    multiSelectedMenuItems: IMenuItem[];
    localMenuItems: IMenuItem[];
    items: ISellItem[];
    selectedItemIndexes: number[];
    totals: ITotal[];
    prompt: string;
    hideCustomer: boolean;
    transaction: Transaction;
    checkoutButtonText: string;
}
