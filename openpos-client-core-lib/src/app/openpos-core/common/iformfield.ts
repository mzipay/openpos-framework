import { IMaskSpec } from './textmask';
export interface IFormElement {
    elementType: string;
    inputType: string;
    label: string;
    id: string;
    value: string;
    values: string[];
    placeholder: string;
    buttonAction: string;
    submitButton: boolean;
    required: boolean;
    selectedIndexes:  number[];
    valueChangedAction: string;
    mask: IMaskSpec;
    pattern: string;
}

