import { Component,Input } from '@angular/core';
import {Product} from './product';
import {MediaChange, ObservableMedia} from "@angular/flex-layout";

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
  gutterSize: number = 20;
  gridColumns: number = 3;  
  products = Product.getProducts();

  constructor(public media: ObservableMedia) {
    
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

}


