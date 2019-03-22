import { IAbstractScreen } from '../../core/interfaces/abstract-screen.interface';
import { IActionItem } from '../../core';
import { ILine } from '../../screens-deprecated/dialog/line.interface';

export interface DialogInterface extends IAbstractScreen {
    buttons: IActionItem[];
    message: string[];
    messageLines: ILine[];
}
