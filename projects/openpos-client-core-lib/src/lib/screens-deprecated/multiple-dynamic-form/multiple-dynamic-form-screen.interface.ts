import { ISellScreen } from '../templates/';
import { IForm, IActionItem } from '../../core';

export interface IMultipleDynamicFormScreen extends ISellScreen {
    forms: IMultipleFormOption[];
    submittedForm: IForm;
}

export interface IMultipleFormOption {
    form: IForm;
    icon: string;
    name: string;
    submitButton: IActionItem;
    submitAction: string;
    instructions: string;
}
