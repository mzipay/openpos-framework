import { IMenuItem } from './menu-item.interface';
import { IItem } from './item.interface';

export interface ISellItem extends IItem {
    posItemId: string;
    quantity: number;
    imageUrl: string;
    productDescription: string;
    menuItems: IMenuItem[];
    sellingPrice: string;
    color: string;
    size: string;
    longDescription: string;
}
