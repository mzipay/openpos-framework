
import { IForm } from '../../common/iform';
import { ISellScreen } from '../templates/sell-template/sell/iSellScreen';


export interface IMultipleDynamicFormScreen extends ISellScreen {
    forms: IMultipleFormOption[];
    submittedForm: IForm;
}

export interface IMultipleFormOption {
    form: IForm;
    icon: string;
    name: string;
    submitAction: string;
    instructions: string;
}