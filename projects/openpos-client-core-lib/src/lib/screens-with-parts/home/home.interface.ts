import { IAbstractScreen } from '../../core/interfaces/abstract-screen.interface';
import { IActionItem } from '../../core/interfaces/menu-item.interface';


export interface HomeInterface extends IAbstractScreen {
    menuItems: IActionItem[];
    backgroundImage: string;
}
