import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { ProductListComponent, ItemClickAction, MenuClickAction } from '../../shared/';
import { SelectionMode, IItem, IActionItem  } from '../../core';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
    selector: 'app-item-list',
    templateUrl: './item-list.component.html'
})
export class ItemListComponent extends PosScreen<any> implements OnInit, OnDestroy {

    items: IItem[];
    itemActionName: string;
    text: string;
    itemActions: IActionItem[] = [];
    condensedListDisplay: false;
    selectionMode: string;
    localMenuItems: IActionItem[];
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
        if (this.getSelectionModeAsEnum() === SelectionMode.Multiple || this.getSelectionModeAsEnum() === SelectionMode.SingleCheckbox) {
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

    isItemSelectedDisabled(): boolean {
        return this.getSelectionModeAsEnum() !== SelectionMode.Multiple
        && this.getSelectionModeAsEnum() !== SelectionMode.SingleCheckbox;
    }

    isItemClickDisabled(): boolean {
        return this.itemActionName === null
        || this.getSelectionModeAsEnum() === SelectionMode.Multiple
        || this.getSelectionModeAsEnum() === SelectionMode.SingleCheckbox;
    }
}
