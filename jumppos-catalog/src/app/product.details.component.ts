import { SessionService } from './session.service';
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
    public sanitizer: DomSanitizer,
    private session: SessionService 
  ) {
    this.route.params.subscribe(params => {
      this.product = Product.getProduct(params['itemId']);
      this.productFeatures = Product.getProductFeatures(params['itemId']);
      this.productSpecifications = Product.getProductSpecifications(params['itemId']);
    });
}

product:Product;
productFeatures:string[];
productSpecifications:string[];
restUrl = "";
productInCart = false;

getRestUrl() {
  let restUrlTemp = this.restUrl;
  if (this.restUrl && this.restUrl.startsWith("http:")) { // Avoid issue where page refreshes are adding multiple items to the cart.
    this.restUrl = "";
  }
  return restUrlTemp;
}

addProductToCart() {
  const nodeId: string = this.session.nodeId;  
  let url = `http://${window.location.hostname}:8080/app/kiosk/node/${nodeId}/AddItem/${this.product.itemId}`;
  console.log('Add to cart ' + url);
  this.restUrl = url;
  this.productInCart = true;
}

}


