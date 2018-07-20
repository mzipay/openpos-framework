
import { SelfCheckoutHomeComponent } from './self-checkout-home/self-checkout-home.component';
import { SelfCheckoutPaymentStatusComponent } from './self-checkout-payment-status/self-checkout-payment-status.component';
import { SelfCheckoutTransactionComponent } from './self-checkout-transaction/self-checkout-transaction.component';
import { SelfCheckoutOptionsComponent } from './self-checkout-options/self-checkout-options.component';
import { SelfCheckoutWithBarComponent } from './self-checkout-with-bar/self-checkout-with-bar.component';

export const ScreenConstants = {
    screens : [
        { name: 'SelfCheckoutHome', component: SelfCheckoutHomeComponent },
        { name: 'SelfCheckoutPaymentStatus', component: SelfCheckoutPaymentStatusComponent },
        { name: 'SelfCheckoutTransaction', component: SelfCheckoutTransactionComponent },
        { name: 'SelfCheckoutOptions', component: SelfCheckoutOptionsComponent },
    ],

    dialogs: [
        { name: 'SelfCheckoutHome', component: SelfCheckoutHomeComponent },
        { name: 'SelfCheckoutTransaction', component: SelfCheckoutTransactionComponent },
        { name: 'SelfCheckoutOptions', component: SelfCheckoutOptionsComponent },
    ],

    templates: [
        { name: 'SelfCheckout', component: SelfCheckoutWithBarComponent }
    ]

};
