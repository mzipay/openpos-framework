import { ISellItem } from '../../isellitem';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-checkout-list-item',
  templateUrl: './checkout-list-item.component.html',
})
export class CheckoutListItemComponent {

  @Input() item: ISellItem;

  constructor() {

  }

}
