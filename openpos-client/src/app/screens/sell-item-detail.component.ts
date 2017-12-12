import { IScreen } from '../common/iscreen';
import {Component} from '@angular/core';
import { ISellItem } from '../common/isellitem';
import {SessionService} from '../services/session.service';
import { AbstractApp } from '../common/abstract-app';

@Component({
  selector: 'app-sell-item-detail',
  templateUrl: './sell-item-detail.component.html'
})

export class SellItemDetailComponent implements IScreen {
  public item: ISellItem;

  constructor(public session: SessionService ) {
    this.item = session.screen.item;
  }

  show(session: SessionService, app: AbstractApp) {
  }

}
