import { IItem } from './iitem';

export interface ISellItem extends IItem {
    posItemId: string;
    quantity: number;
}
