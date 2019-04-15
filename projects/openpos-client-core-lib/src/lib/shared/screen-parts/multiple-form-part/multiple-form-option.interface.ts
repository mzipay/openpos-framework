import { IActionItem } from '../../../core/interfaces/menu-item.interface';
import { IForm } from '../../../core/interfaces/form.interface';

export interface IMultipleFormOption {
    form: IForm;
    icon: string;
    name: string;
    submitButton: IActionItem;
    submitAction: string;
    instructions: string;
}
