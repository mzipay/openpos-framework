import { IAbstractScreen } from '../../core/interfaces/abstract-screen.interface';
import { IActionItem } from '../../core';
import { SelectionListItem } from './selection-list-item.interface';

export interface SelectionListInterface extends IAbstractScreen {
    selectionList: SelectionListItem[];
    buttons: IActionItem[];
    nonSelectionButtons: IActionItem[];
    multiSelect: boolean;
    defaultSelect: boolean;
    defaultSelectItemIndex: number;
    selectionChangedAction: string;
    instructions: string;
}
