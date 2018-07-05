import {Component} from '@angular/core';
import {MatTableDataSource} from '@angular/material';
import { IScreen } from '../../common/iscreen';
import { ISellItem } from '../../common/isellitem';
import { IPromoItem } from '../../common/ipromoitem';
import { SessionService } from '../../services/session.service';

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

  show(screen: any) {
    this.screen = screen;
    this.item = screen.item;
    this.promosDataSource = new MatTableDataSource(this.screen.promos);
  }

}
