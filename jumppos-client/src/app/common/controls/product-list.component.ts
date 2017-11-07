import { SelectionMode } from './../selectionmode';
import { IMenuItem } from './../imenuitem';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { IScreen } from '../iscreen';
import { IItem } from '../iitem';

@Component({
    selector: 'app-product-list',
    templateUrl: './product-list.component.html'
})
export class ProductListComponent {
    @Input() items: IItem[];
    @Input() menuItemActions: IMenuItem[] = [];
    @Input() showItemIcon = true;
    @Input() showItemMenu = false;
    @Output() itemClick = new EventEmitter<ItemClickAction>();
    @Output() menuItemClick = new EventEmitter<MenuClickAction>();
    @Input() selectionMode: SelectionMode = SelectionMode.Single;

    onItemClick(item: IItem, event: any): void {
        console.log(`productList.onItemClick: ${item}`);
        this.itemClick.emit({item, event});
    }

    onMenuItemClick(item: IItem, menuItem: IMenuItem, event: any): void {
        console.log(`productList.onItemClick: ${menuItem}`);
        this.menuItemClick.emit({item, menuItem, event});
    }
}

export interface ItemClickAction {
    item: IItem;
    event: any;
}

export interface MenuClickAction {
    item: IItem;
    menuItem: IMenuItem;
    event: any;
}
