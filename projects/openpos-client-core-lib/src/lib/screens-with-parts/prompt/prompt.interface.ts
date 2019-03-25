
import { Validator } from '../../core/interfaces/validator.enum';
import { IAbstractScreen } from '../../core/interfaces/abstract-screen.interface';
import { FieldInputType } from '../../core/interfaces/field-input-type.enum';
import { IActionItem } from '../../core/interfaces/menu-item.interface';

export interface PromptInterface extends IAbstractScreen {
    promptIcon: string;
    placeholderText: string;
    hintText: string;
    instructions: string;
    responseText: string;
    editable: boolean;
    responseType: FieldInputType;
    actionButton: IActionItem;
    otherActions: IActionItem[];
    minLength: number;
    maxLength: number;
    comments: string;
    showComments: boolean;
    validationPattern: string;
    scanEnabled: boolean;
    keyboardPreference: string;
    validators: Validator[];
    sausageLinks: IActionItem[];
}
