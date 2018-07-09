import { IOptionItem } from './option-item.interface';
import { IMenuItem } from '../../core';


export interface IChooseOptionsScreen {
    options: IOptionItem[];
    selectionMode: SelectionMode;
    displayStyle: DisplayStyle;
    promptText: string;
    responseType: string;
    responseText: string;
    showComments: string;
    comments: string;
    actionButton: IMenuItem;
    type: string;
}

export enum SelectionMode{
    None="None",
    Single="Single",
    Multiple="Multiple",
}

export enum DisplayStyle{
    ButtonList="ButtonList",
    ButtonGrid="ButonGrid"
}