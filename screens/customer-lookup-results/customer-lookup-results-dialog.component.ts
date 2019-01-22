
import { Component } from '@angular/core';
import { IScreen } from '../../core/components/dynamic-screen/screen.interface';
import { SelectableItemListComponentConfiguration } from '../../shared/components/selectable-item-list/selectable-item-list.component';
import { SessionService } from '../../core/services/session.service';
import { SelectionMode } from '../../core/interfaces/selection-mode.enum';
import { ISearchResult } from './search-result.interface';
import { ICustomerLookupResultsScreen } from './customer-lookup-results-screen.interface';

@Component({
    selector: 'app-customer-lookup-results-dialog',
    templateUrl: './customer-lookup-results-dialog.component.html'
})
export class CustomerLookupResultsDialogComponent implements IScreen {

    screen: ICustomerLookupResultsScreen;
    selectedResult: ISearchResult;
    listConfig = new SelectableItemListComponentConfiguration<ISearchResult>();

    constructor(private session: SessionService) {
    }

    show(screen: any): void {
        this.screen = screen;
        if (this.screen.selectedCustomerIndex !== null && this.screen.selectedCustomerIndex !== undefined) {
            this.listConfig.defaultSelectItemIndex = this.screen.selectedCustomerIndex;
        }
        this.listConfig.items = this.screen.customers;
        this.listConfig.numResultsPerPage = 5;
        this.listConfig.selectionMode = SelectionMode.Single;
    }

    onNext() {
        this.session.onAction('ResultSelected', this.selectedResult.id);
    }

    onBack() {
        if (this.screen.hasLoyaltyCustomerLinked) {
            this.session.onAction('BackLoyalty');
        } else {
            this.session.onAction('Back');
        }
    }

    onEdit() {
        this.session.onAction('ResultEdit', this.selectedResult.id);
    }

    onAdd() {
        this.session.onAction('AddLoyaltyCustomerToLocal');
    }
}
