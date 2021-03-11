import {Component, Input} from '@angular/core';
import {CustomerDetails} from "./customer-information.interface";


@Component({
    selector: 'app-customer-information',
    templateUrl: './customer-information.component.html',
    styleUrls: ['./customer-information.component.scss']})
export class CustomerInformationComponent {
    @Input()
    customer: CustomerDetails;
    constructor() {}
}
