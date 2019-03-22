import { SelectionListItemDisplayProperty } from './selection-list-item-display-property.interface';
import { IActionItem } from '../../core';

export interface SelectionListItem {
    title: string;
    properties: SelectionListItemDisplayProperty[];
    isSelected: boolean;
    enabled: boolean;
    itemImageUrl: string;
}
