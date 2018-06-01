import { IItem } from './iitem';

export interface ITenderItem extends IItem {
    type: string;
    number: number;
}
