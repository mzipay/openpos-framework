import { IAbstractScreen } from '../../../core/interfaces/abstract-screen.interface';
import { IActionItem } from '../../../core/interfaces/menu-item.interface';

export interface SelfCheckoutOptionsPartInterface extends IAbstractScreen {

    options: IActionItem[];
    additionalButtons: IActionItem[];
    linkButtons: IActionItem[];
}
