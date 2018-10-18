import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { ProductListComponent, ItemClickAction, MenuClickAction } from '../../shared/';
import { SelectionMode, IItem, IMenuItem  } from '../../core';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
    selector: 'app-item-list',
    templateUrl: './item-list.component.html'
})
export class ItemListComponent extends PosScreen<any> implements OnInit, OnDestroy {

    items: IItem[];
    itemActionName: string;
    text: string;
    itemActions: IMenuItem[] = [];
    condensedListDisplay: false;
    selectionMode: string;
    localMenuItems: IMenuItem[];
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
        this.localMenuItems = this.screen.template.localMenuItems;
        this.selectionMode = this.screen.selectionMode;

    }

    ngOnInit(): void {
    }

    ngOnDestroy(): void {
        if (this.localMenuItems) {
            this.localMenuItems.forEach(element => {
                this.session.unregisterActionPayloads();
            });
        }
    }

    getSelectionModeAsEnum(): SelectionMode {
        if (this.selectionMode) {
            return SelectionMode[this.selectionMode];
        } else {
            return SelectionMode.Single;
        }
    }

    onItemClick(itemInfo: ItemClickAction): void {
        this.session.onAction(this.itemActionName, itemInfo.item);
    }

    onItemSelected(itemInfo: ItemClickAction): void {
        if (this.getSelectionModeAsEnum() === SelectionMode.Multiple) {
            if (this.localMenuItems) {
                this.localMenuItems.forEach(element => {
                    this.session.registerActionPayload(element.action, () => this.productList.selectedItems);
                });
            }
        }
    }

    onActionButtonClick(): void {
        this.session.onAction(this.screen.actionButton.action, this.productList.selectedItems);
    }

    onMenuItemClick(itemInfo: MenuClickAction): void {
        this.session.onAction(itemInfo.menuItem, itemInfo.item);
    }

    isItemSelectedDisabled(): boolean {
        return this.getSelectionModeAsEnum() !== SelectionMode.Multiple;
    }

    isItemClickDisabled(): boolean {
        return this.itemActionName === null || this.getSelectionModeAsEnum() === SelectionMode.Multiple;
    }
}
