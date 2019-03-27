import { FieldInputType } from '../../../core/interfaces/field-input-type.enum';
import { IActionItem } from '../../../core/interfaces/menu-item.interface';
import { Validator } from '@angular/forms';

export interface PromptFormPartInterface {
    type: string;
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
    validationPattern: string;
    scanEnabled: boolean;
    keyboardPreference: string;
    validators: Validator[];
}
