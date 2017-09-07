import { Component,Input } from '@angular/core';
import {ActivatedRoute, Router, Params} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {Product} from './product';
import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'product-details',
  templateUrl: './product.details.component.html'
})
export class ProductDetailsComponent {
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    public sanitizer: DomSanitizer 
  ) {
    this.route.params.subscribe(params => {
      console.log("Item ID is: " + params['itemId']);
      this.product = Product.getProduct(params['itemId']);
    });
}

product:Product;
restUrl:string;

addProductToCart() {
  console.log('Add to cart');
  this.restUrl = 'http://localhost:8080/app/kiosk/node/05243-013/AddItem/' + this.product.itemId;
}

}


