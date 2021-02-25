import {Component} from '@angular/core';
import { DialogComponent } from '../../shared/decorators/dialog-component.decorator';
import {PosScreen} from "../pos-screen/pos-screen.component";
import {IActionItem} from "../../core/actions/action-item.interface";
import {Observable} from "rxjs/internal/Observable";
import {SelectableItemListComponentConfiguration} from "../../shared/components/selectable-item-list/selectable-item-list.component";
import {SelectionMode} from "../../core/interfaces/selection-mode.enum";
import {CustomerSearchResultDialogInterface, ICustomerDetails} from "./customer-search-result-dialog.interface";
import {ISelectableListData} from "../../shared/components/selectable-item-list/selectable-list-data.interface";

@DialogComponent({
    name: 'CustomerSearchResultDialog'
})
@Component({
    selector: 'app-customer-search-result-dialog',
    templateUrl: './customer-search-result-dialog.component.html',
    styleUrls: ['./customer-search-result-dialog.component.scss']
})
export class CustomerSearchResultDialogComponent extends PosScreen<CustomerSearchResultDialogInterface> {

    index = -1;
    listData: Observable<ISelectableListData<ICustomerDetails>>;

    listConfig: SelectableItemListComponentConfiguration;

    buildScreen() {

        let allItems = new Map<number, ICustomerDetails>();
        let disabledItems = new Map<number, ICustomerDetails>();
        for (let i = 0; i < this.screen.results.length; i++){
            allItems.set(i, this.screen.results[i]);
        }

        this.listData = new Observable<ISelectableListData<ICustomerDetails>>((observer) => {
            observer.next({
                items: allItems,
                disabledItems: disabledItems
            } as ISelectableListData<ICustomerDetails>);
        });

        this.listConfig = new SelectableItemListComponentConfiguration();
        this.listConfig.selectionMode = SelectionMode.Single;
        this.listConfig.numItemsPerPage = Number.MAX_VALUE;
        this.listConfig.totalNumberOfItems = 3;

    }

    public onItemChange(event: any): void {
        this.index = event;
    }

    public doSelectionButtonAction(menuItem: IActionItem) {
        this.doAction(menuItem, this.index);
    }
}