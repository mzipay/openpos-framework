import { IOptionItem } from './option-item.interface';
import { IActionItem, IAbstractScreen } from '../../core';


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
