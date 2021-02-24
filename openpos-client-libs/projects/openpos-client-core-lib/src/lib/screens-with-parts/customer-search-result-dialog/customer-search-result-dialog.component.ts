import { Component, Injector } from '@angular/core';
import { DialogComponent } from '../../shared/decorators/dialog-component.decorator';
import {SelectionListScreenComponent} from "../selection-list/selection-list-screen.component";


@DialogComponent({
    name: 'CustomerSearchResultDialog'
})
@Component({
    selector: 'app-customer-search-result-dialog',
    templateUrl: './customer-search-result-dialog.component.html',
    styleUrls: ['./customer-search-result-dialog.component.scss']
})
export class CustomerSearchResultDialogComponent extends SelectionListScreenComponent {

}