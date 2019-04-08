import { IAbstractScreen } from '../../core/interfaces/abstract-screen.interface';
import { IActionItem } from '../../core/interfaces/menu-item.interface';


export interface SelfCheckoutFormInterface extends IAbstractScreen {
    submitButton: IActionItem;
    alternateSubmitActions: IActionItem[];
}
