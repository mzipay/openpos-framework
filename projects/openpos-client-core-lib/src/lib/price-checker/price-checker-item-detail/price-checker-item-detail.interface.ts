import { IActionItem } from '../../core/interfaces/menu-item.interface';
import { DisplayProperty } from '../../shared/components/display-property/display-property.interface';
import { IAbstractScreen } from '../../core/interfaces/abstract-screen.interface';

export interface PriceCheckerItemDetailInterface extends IAbstractScreen {
    printButton: IActionItem;
    itemDescription: string;
    itemProperties: DisplayProperty[];
    pricePerUnit: DisplayProperty;
    currentPrice: DisplayProperty;
    associatedItems: DisplayProperty[];
    logoUrl: string;
    itemMessages: string[];
    disclaimer: string;
}
