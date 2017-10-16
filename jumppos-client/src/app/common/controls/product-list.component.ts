import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { IScreen } from '../iscreen';
import { IItem } from '../iitem';

@Component({
    selector: 'app-product-list',
    templateUrl: './product-list.component.html'
})
export class ProductListComponent {
    @Input() items: IItem[];
    @Output() itemClick = new EventEmitter<IItem>();

    onItemClick(item: IItem): void {
        console.log(`productList.onItemClick: ${item}`)
        this.itemClick.emit(item);
    }
}
