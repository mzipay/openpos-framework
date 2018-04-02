import { IScreen } from '../common/iscreen';
import {Component} from '@angular/core';
import { ISellItem } from '../common/isellitem';
import {SessionService} from '../services/session.service';
import { AbstractApp } from '../common/abstract-app';
import { IPromoItem } from '../common/ipromoitem';
import {MatTableDataSource} from '@angular/material';

@Component({
  selector: 'app-sell-item-detail',
  templateUrl: './sell-item-detail.component.html',
  styleUrls: ['./sell-item-detail.component.scss']
})

export class SellItemDetailComponent implements IScreen {
  public item: ISellItem;

  screen: any;
  promosDataSource : MatTableDataSource<IPromoItem>;

  constructor(public session: SessionService ) {
  }

  show(screen: any, app: AbstractApp) {
    this.screen = screen;
    this.item = screen.item;
  }

  ngDoCheck(): void {
      if (typeof this.screen !== 'undefined') {
        // this.items = this.screen.items;
        this.promosDataSource = new MatTableDataSource(this.screen.promos);
      }
  }

}
