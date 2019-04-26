import { IAbstractScreen } from '../../core/interfaces/abstract-screen.interface';
import { IActionItem } from '../../core/interfaces/menu-item.interface';


export interface PriceCheckerHomeInterface extends IAbstractScreen {
    backgroundImageUrl: string;
    scanAction: IActionItem;
}
