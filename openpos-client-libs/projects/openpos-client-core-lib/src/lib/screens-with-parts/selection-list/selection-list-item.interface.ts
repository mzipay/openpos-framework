import { SelectionListItemDisplayProperty } from './selection-list-item-display-property.interface';
import {SelectableItemInterface} from "./selectable-item.interface";


export interface ISelectionListItem extends SelectableItemInterface {
    title: string;
    properties: SelectionListItemDisplayProperty[];
    itemImageUrl: string;
}
