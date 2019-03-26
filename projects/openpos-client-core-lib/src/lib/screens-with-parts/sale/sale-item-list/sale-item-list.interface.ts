import { IActionItem } from '../../../core/interfaces/menu-item.interface';
import { ISellItem } from '../../../core/interfaces/sell-item.interface';
import { IActionItemGroup } from '../../../core/interfaces/action-item-group.interface';


export interface SaleItemListInterface {
    multiSelectedMenuItems: IActionItem[];
    items: ISellItem[];
    selectedItemIndexes: number[];
    transactionMenuPrompt: string;
    transactionMenu: IActionItemGroup;
}
