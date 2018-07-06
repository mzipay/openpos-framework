import { Component, OnInit, ViewChild } from '@angular/core';
import { IItem } from '../../common/iitem';
import { IMenuItem } from '../../common/imenuitem';
import { ProductListComponent, ItemClickAction, MenuClickAction } from '../../shared/';
import { SelectionMode } from '../../common/selectionmode';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
    selector: 'app-item-list',
    templateUrl: './item-list.component.html'
})
export class ItemListComponent extends PosScreen<any> implements OnInit {

    items: IItem[];
    itemActionName: string;
    text: string;
    itemActions: IMenuItem[] = [];
    condensedListDisplay: false;
    selectionMode: string;
    @ViewChild('productList') productList: ProductListComponent;

    constructor() {
        super();
    }

    buildScreen() {
        this.items = this.screen.items;
        this.itemActionName = this.screen.itemActionName;
        this.text = this.screen.text;
        this.itemActions = this.screen.itemActions;
        this.condensedListDisplay = this.screen.condensedListDisplay;
        this.selectionMode = this.screen.selectionMode;
    }

    ngOnInit(): void {
    }

    getSelectionModeAsEnum(): SelectionMode {
        if (this.selectionMode) {
            return SelectionMode[this.selectionMode];
        } else {
            return SelectionMode.Single;
        }
    }

    onItemClick(itemInfo: ItemClickAction): void {
        this.session.response = itemInfo.item;
        this.session.onAction(this.itemActionName);
    }

    onItemSelected(itemInfo: ItemClickAction): void {
        if (this.getSelectionModeAsEnum() === SelectionMode.Multiple) {
            this.session.response = this.productList.selectedItems;
        }
    }

    onActionButtonClick(): void {
        this.session.onAction(this.screen.actionButton.action, null);
    }

    onMenuItemClick(itemInfo: MenuClickAction): void {
        this.session.response = itemInfo.item;
        this.session.onAction(itemInfo.menuItem.action, null, itemInfo.menuItem.confirmationMessage );
    }

    isItemSelectedDisabled(): boolean {
        return this.getSelectionModeAsEnum() !== SelectionMode.Multiple;
    }

    isItemClickDisabled(): boolean {
        return this.itemActionName === null || this.getSelectionModeAsEnum() === SelectionMode.Multiple;
    }
}
