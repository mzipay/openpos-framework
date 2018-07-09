import { IItem } from '../../core';

export interface ITenderItem extends IItem {
    type: string;
    number: number;
}
