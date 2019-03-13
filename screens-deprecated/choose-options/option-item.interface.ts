import { IForm } from '../../core';

export interface IOptionItem {
    displayValue: String;
    value: string;
    enabled: boolean;
    selected: boolean;
    icon: String;
    form: IForm;
    keybind: String;
}
