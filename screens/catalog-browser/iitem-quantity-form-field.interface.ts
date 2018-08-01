import { IFormElement } from '../../core';

export interface IItemQuantityFormElement extends IFormElement {
    min: number;
    max: number;
}
