import { IForm } from '../../common/iform';

export interface IOptionItem {
    displayValue: String;
    value: string;
    enabled: boolean;
    selected: boolean;
    icon: String;
    form: IForm;
}