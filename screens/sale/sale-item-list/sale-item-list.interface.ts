import { IActionItem, ISellItem } from '../../../core/interfaces';

export interface SaleItemListInterface {
    multiSelectedMenuItems: IActionItem[];
    items: ISellItem[];
    selectedItemIndexes: number[];
    transactionMenuPrompt: string;
    transactionMenuItems: IActionItem[];
}
