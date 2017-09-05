import { IScreen } from './../common/iscreen';
import { Component } from '@angular/core';
import { SessionService } from '../session.service';

@Component({
    selector: 'app-cart',
    templateUrl: './cart.component.html'
})
export class CartComponent implements IScreen {

    constructor(public session: SessionService) {
    }

    show(session: SessionService) {
    }

}
