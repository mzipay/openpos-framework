import {Component} from '@angular/core';
import {ISellItem} from './sell.component';
import {SessionService} from '../session.service';

@Component({
  selector: 'app-sell-item-detail',
  templateUrl: './sell-item-detail.component.html'
})

export class SellItemDetailComponent {
  public sellItems: ISellItem[];

  constructor(public session: SessionService ) {
    this.sellItems = session.screen.items;
  }
}
