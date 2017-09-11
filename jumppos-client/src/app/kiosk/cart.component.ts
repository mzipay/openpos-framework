import { ISellItem } from './../common/isellitem';
import { IScreen } from './../common/iscreen';
import { Component } from '@angular/core';
import { SessionService } from '../session.service';
import {DataSource} from '@angular/cdk/collections';
import {Observable} from 'rxjs/Observable';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';

@Component({
    selector: 'app-cart',
    templateUrl: './cart.component.html'
})
export class CartComponent implements IScreen {

    public dataSource: TableDataSource;
    displayedColumns = [ 'description', 'extendedAmount', 'quantity'];

    constructor(public session: SessionService) {
        this.dataSource = new TableDataSource();
    }

    show(session: SessionService) {
        console.log('populating datasource with ' + this.session.screen.cart.items.length);
        this.dataSource.dataChange.next(this.session.screen.cart.items);
    }

}

export class TableDataSource extends DataSource<ISellItem> {

    public dataChange: BehaviorSubject<ISellItem[]>  = new BehaviorSubject<ISellItem[]>([]);

    connect(): Observable<ISellItem[]> {
      return this.dataChange;
    }
    disconnect() {}
  }
