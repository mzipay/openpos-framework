import { IActionItem, ISellItem, IActionItemGroup } from '../../../core/interfaces';

export interface SaleItemListInterface {
    multiSelectedMenuItems: IActionItem[];
    items: ISellItem[];
    selectedItemIndexes: number[];
    transactionMenuPrompt: string;
    transactionMenu: IActionItemGroup;
}
