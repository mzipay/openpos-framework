import { Component,Input } from '@angular/core';
import { Http, ResponseContentType } from '@angular/http';
import { ProductCategory } from './product-category';
import {MediaChange, ObservableMedia} from "@angular/flex-layout";

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

  categories = [];
  gutterSize: number = 20;
  gridColumns: number = 3;

  constructor(private http: Http, public media: ObservableMedia) {
     this.categories = ProductCategory.getCategories();
  }

  ngOnInit() {
    this.updateGrid();
    this.media.subscribe(() => {
        this.updateGrid();
    });
  }

  private updateGrid(): void {  
    let isLarge = (this.media.isActive('xl') || this.media.isActive('lg') || this.media.isActive('md'));
    let isSmall = (this.media.isActive('sm'));
    this.gridColumns = isLarge ? 3 : (isSmall ? 2 : 1);
    this.gutterSize = isLarge ? 40 : 10;
  } 

  categoryTapped(categoryId:string) {
    
  }
}
