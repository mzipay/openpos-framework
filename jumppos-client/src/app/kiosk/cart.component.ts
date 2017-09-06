import { ISellItem } from './../common/isellitem';
import { IScreen } from './../common/iscreen';
import { Component } from '@angular/core';
import { SessionService } from '../session.service';
import {DataSource} from '@angular/cdk/collections';
import {Observable} from 'rxjs/Observable';

@Component({
    selector: 'app-cart',
    templateUrl: './cart.component.html'
})
export class CartComponent implements IScreen {

    public dataSource: TableDataSource;
    displayedColumns = [ 'description', 'extendedAmount', 'quantity'];

    constructor(public session: SessionService) {
    }

    show(session: SessionService) {
        console.log('populating datasource with ' + this.session.screen.cart.items.length);
        this.dataSource = new TableDataSource(this.session.screen.cart.items);
    }

}

export class TableDataSource extends DataSource<ISellItem> {

    constructor(public list: ISellItem[]) {
        super();
    }

    connect(): Observable<ISellItem[]> {
      return Observable.of(this.list);
    }
    disconnect() {}
  }
