import { IFormElement } from '../../core/interfaces/form-field.interface';


export interface IItemQuantityFormElement extends IFormElement {
    min: number;
    max: number;
}
