import {Component, Input} from '@angular/core';
import {ICustomerDetails} from "../../../screens-with-parts/customer-search-result-dialog/customer-search-result-dialog.interface";

@Component({
    selector: 'app-display-customer-lookup',
    templateUrl: './display-customer-lookup.component.html',
    styleUrls: ['./display-customer-lookup.component.scss']
})
export class DisplayCustomerLookupComponent { @Input() customer: ICustomerDetails; }