import { ISellItem } from './../common/isellitem';
import { IScreen } from './../common/iscreen';
import { Component } from '@angular/core';
import { SessionService } from '../session.service';
import {DataSource} from '@angular/cdk/collections';
import {Observable} from 'rxjs/Observable';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';

@Component({
    selector: 'app-cart',
    templateUrl: './cart.component.html'
})
export class CartComponent implements IScreen {

    constructor(public session: SessionService) {
    }

    getCartMessage() {
        if (this.session.screen.cart.items == null || this.session.screen.cart.items.length == 0) {
            return "No items in your cart yet.";
        } else if (this.session.screen.cart.items.length == 1) {
            return "1 item in your cart.";
        } else {
            return this.session.screen.cart.items.length + " items in your cart.";
        }
    }

    removeItem(index) {        
        this.session.onActionWithStringPayload('RemoveItem', index);
    }

    show(session: SessionService) {

    }
}