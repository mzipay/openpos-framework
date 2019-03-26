import { IForm } from '../../core/interfaces/form.interface';


export interface IOptionItem {
    displayValue: string;
    value: string;
    enabled: boolean;
    selected: boolean;
    icon: string;
    form: IForm;
    keybind: string;
}
