import { IAbstractScreen, IMenuItem, ISellItem } from '../../core';
import { ITotal } from '../../core/interfaces/total.interface';

export interface SaleInterface extends IAbstractScreen {
    transactionMenuItems: IMenuItem[];
    multiSelectedMenuItems: IMenuItem[];
    localMenuItems: IMenuItem[];
    items: ISellItem[];
    selectedItemIndexes: number[];
    totals: ITotal[];
}
