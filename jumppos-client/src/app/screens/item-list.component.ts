import { ItemClickAction, MenuClickAction } from './../common/controls/product-list.component';
import { IMenuItem } from './../common/imenuitem';
import { Component, OnInit } from '@angular/core';
import { SessionService } from '../session.service';
import { IScreen } from './../common/iscreen';
import { IItem } from './../common/iitem';

@Component({
    selector: 'app-item-list',
    templateUrl: './item-list.component.html'
})
export class ItemListComponent implements IScreen, OnInit {

    items: IItem[];
    itemActionName: string;
    text: string;
    itemActions: IMenuItem[] = [];

    constructor(public session: SessionService) {
    }

    show(session: SessionService) {
    }

    ngOnInit(): void {
        this.items = this.session.screen.items;
        this.itemActionName = this.session.screen.itemActionName;
        this.text = this.session.screen.text;
        this.itemActions = this.session.screen.itemActions;
    }

    onItemClick(itemInfo: ItemClickAction): void {
        this.session.response = itemInfo.item;
        this.session.onAction(this.itemActionName);
    }

    onMenuItemClick(itemInfo: MenuClickAction): void {
        this.session.response = itemInfo.item;
        this.session.onAction(itemInfo.menuItem.action);
    }

    isItemClickDisabled(): boolean {
        return this.itemActionName === null;
    }
}
