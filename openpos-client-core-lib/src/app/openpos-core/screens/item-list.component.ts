import { ItemClickAction, MenuClickAction } from './../common/controls/product-list.component';
import { IMenuItem } from './../common/imenuitem';
import { Component, OnInit, DoCheck } from '@angular/core';
import { SessionService } from '../services/session.service';
import { IScreen } from './../common/iscreen';
import { IItem } from './../common/iitem';
import { AbstractApp } from '../common/abstract-app';

@Component({
    selector: 'app-item-list',
    templateUrl: './item-list.component.html'
})
export class ItemListComponent implements IScreen, OnInit {

    screen: any;
    items: IItem[];
    itemActionName: string;
    text: string;
    itemActions: IMenuItem[] = [];
    condensedListDisplay: false;
    private lastSequenceNum: number;
    selectionMode: string;

    constructor(public session: SessionService) {
    }

    show(screen: any, app: AbstractApp) {
        this.screen = screen;

        this.items = this.screen.items;
        this.itemActionName = this.screen.itemActionName;
        this.text = this.screen.text;
        this.itemActions = this.screen.itemActions;
        this.condensedListDisplay = this.screen.condensedListDisplay;
        this.selectionMode = this.screen.selectionMode;
    }

    ngOnInit(): void {
    }

    onItemClick(itemInfo: ItemClickAction): void {
        this.session.response = itemInfo.item;
        this.session.onAction(this.itemActionName);
    }

    onMenuItemClick(itemInfo: MenuClickAction): void {
        this.session.response = itemInfo.item;
        this.session.onAction(itemInfo.menuItem.action, null, itemInfo.menuItem.confirmationMessage );
    }

    isItemClickDisabled(): boolean {
        return this.itemActionName === null;
    }
}
