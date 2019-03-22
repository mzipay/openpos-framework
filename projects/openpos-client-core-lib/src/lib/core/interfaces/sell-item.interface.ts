import { IActionItem } from './menu-item.interface';
import { IItem } from './item.interface';

export interface ISellItem extends IItem {
    posItemId: string;
    quantity: number;
    imageUrl: string;
    productDescription: string;
    menuItems: IActionItem[];
    sellingPrice: string;
    color: string;
    size: string;
    longDescription: string;
    type: string;
    prop65Item: boolean;
    prop65WarningText: string;
    styleNumber: string;
}
