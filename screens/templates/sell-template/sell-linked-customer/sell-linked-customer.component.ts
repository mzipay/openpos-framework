import { Component, Input } from '@angular/core';
import { IMenuItem, SessionService } from '../../../../core';

@Component({
    selector: 'app-sell-linked-customer',
    templateUrl: './sell-linked-customer.component.html',
    styleUrls: ['./sell-linked-customer.component.scss']
})

export class SellLinkedCustomerComponent {
    @Input() screenType: string;
    @Input() customerName: string;
    @Input() noCustomerText: string;
    @Input() loyaltyButton?: IMenuItem;

    constructor(public session: SessionService) { }

    onClick() {
        if (this.loyaltyButton) {
            this.session.onAction(this.loyaltyButton);
        }
    }
}
