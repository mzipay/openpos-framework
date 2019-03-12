import { IActionItem } from './menu-item.interface';

export interface IActionItemGroup {
    title: string;
    keybind: string;
    actionItems: IActionItem[];
}
