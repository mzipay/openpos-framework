import { IOptionItem } from './option-item.interface';
import { IAbstractScreen } from '../../core/interfaces/abstract-screen.interface';
import { IActionItem } from '../../core/interfaces/menu-item.interface';


export interface IChooseOptionsScreen extends IAbstractScreen {
    options: IOptionItem[];
    selectionMode: SelectionMode;
    displayStyle: DisplayStyle;
    promptText: string;
    responseType: string;
    responseText: string;
    showComments: string;
    comments: string;
    actionButton: IActionItem;
    promptIcon: string;
    prompt: string;
    promptMask: string;
    minLength: string;
    maxLength: string;
    readOnly: boolean;
    placeholderText: string;
    backButton: IActionItem;
}

export enum SelectionMode {
    None= 'None',
    Single= 'Single',
    Multiple= 'Multiple',
}

export enum DisplayStyle {
    ButtonList= 'ButtonList',
    ButtonGrid= 'ButonGrid'
}
