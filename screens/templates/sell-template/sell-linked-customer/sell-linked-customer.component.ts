import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-sell-linked-customer',
    templateUrl: './sell-linked-customer.component.html',
    styleUrls: ['./sell-linked-customer.component.scss']
  })

export class SellLinkedCustomerComponent {
    @Input() screenType: string;
    @Input() customerName: string;
    @Input() noCustomerText: string;
}
