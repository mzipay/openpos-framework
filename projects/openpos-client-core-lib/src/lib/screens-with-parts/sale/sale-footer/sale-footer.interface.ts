import { IActionItem } from '../../../core';
import { DisplayProperty } from '../../../shared';

export interface SaleFooterInterface {
    itemCount: string;
    grandTotal: DisplayProperty;
    checkoutButton: IActionItem;
}
