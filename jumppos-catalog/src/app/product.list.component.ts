import { Component,Input } from '@angular/core';
import {Product} from './product';
import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations';

@Component({
  selector: 'product-list',
  templateUrl: './product.list.component.html'
})

export class ProductListComponent {
  title = 'home';

  products = Product.getProducts();
}
