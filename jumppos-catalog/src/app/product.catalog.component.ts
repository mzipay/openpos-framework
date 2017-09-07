import { Component,Input } from '@angular/core';
import { Http, ResponseContentType } from '@angular/http';
import { ProductCategory } from './product-category';

import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations';

@Component({
  selector: 'product-catalog',
  templateUrl: './product.catalog.component.html'
})
export class ProductCatalogComponent {
  constructor(private http: Http) {
    console.log('ProductCatalogComponent constructed.');
     this.categories = ProductCategory.getCategories();
  }

  categoryTapped(categoryId:string) {
    
  }

  categories = [];

}
