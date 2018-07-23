import {Component} from '@angular/core';
import {MatTableDataSource} from '@angular/material';
import { ISellItem } from '../../core';
import { IPromoItem } from './promo-item.interface';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
  selector: 'app-sell-item-detail',
  templateUrl: './sell-item-detail.component.html',
  styleUrls: ['./sell-item-detail.component.scss']
})

export class SellItemDetailComponent extends PosScreen<any> {
  public item: ISellItem;

  promosDataSource: MatTableDataSource<IPromoItem>;

  constructor() {
      super();
  }

  buildScreen() {
    this.item = this.screen.item;
    this.promosDataSource = new MatTableDataSource(this.screen.promos);
  }

}
