import { IMaskSpec } from '../../shared/textmask';
import { IConfirmationDialog } from './confirmation-dialog.interface';
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
    minLength: number;
    maxLength: number;
    iconName: string;
    disabled: boolean;
    select: boolean;
    checked: boolean;
    keyboardPreference: string;
    confirmationDialog: IConfirmationDialog;
    confirmationMessage: string;
    scanEnabled: boolean;
    validators?: string[];
}

export type LabelPositionType = 'before' | 'after';
export interface ICheckboxField extends IFormElement {
    labelPosition: LabelPositionType;
    checked: boolean;
}

