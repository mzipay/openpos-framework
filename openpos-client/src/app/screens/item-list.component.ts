import { ItemClickAction, MenuClickAction } from './../common/controls/product-list.component';
import { IMenuItem } from './../common/imenuitem';
import { Component, OnInit, DoCheck } from '@angular/core';
import { SessionService } from '../services/session.service';
import { IScreen } from './../common/iscreen';
import { IItem } from './../common/iitem';
import { AbstractApp } from './abstract-app';

@Component({
    selector: 'app-item-list',
    templateUrl: './item-list.component.html'
})
export class ItemListComponent implements IScreen, OnInit, DoCheck {

    items: IItem[];
    itemActionName: string;
    text: string;
    itemActions: IMenuItem[] = [];
    condensedListDisplay: false;
    private lastSequenceNum: number;

    constructor(public session: SessionService) {
    }

    show(session: SessionService, app: AbstractApp) {
    }

    ngOnInit(): void {
        this.items = this.session.screen.items;
        this.itemActionName = this.session.screen.itemActionName;
        this.text = this.session.screen.text;
        this.itemActions = this.session.screen.itemActions;
        this.condensedListDisplay = this.session.screen.condensedListDisplay;
    }

    ngDoCheck(): void {
        if (this.session.screen.type === 'ItemList'
            && this.session.screen.sequenceNumber !== this.lastSequenceNum) {
          this.ngOnInit();
          this.lastSequenceNum = this.session.screen.sequenceNumber;
        }
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
