import { DeviceService } from '../../services/device.service';
import { ISellItem } from '../../common/isellitem';
import { IScreen } from '../../common/iscreen';
import { IMenuItem } from '../../common/imenuitem';
import { Component, ViewChild, AfterViewInit, DoCheck} from '@angular/core';
import { SessionService } from '../../services/session.service';
import { AbstractApp } from '../../common/abstract-app';
import { ITenderItem } from '../../common/itenderitem';
import {MatTableDataSource} from '@angular/material';

@Component({
  selector: 'app-change',
  templateUrl: './change.component.html',
  styleUrls: ['./change.component.scss']
})
export class ChangeComponent implements AfterViewInit, DoCheck, IScreen {

  total : string;
  tendered : string;
  balanceDue : string;

  itemsDataSource : MatTableDataSource<ITenderItem>;
  // public items: ITenderItem[];

  constructor(public session: SessionService, devices: DeviceService) {

  }

  show(session: SessionService, app: AbstractApp) {
  }

  ngDoCheck(): void {
      if (typeof this.session.screen !== 'undefined') {
        // this.items = this.session.screen.items;
        this.itemsDataSource = new MatTableDataSource(this.session.screen.items);
      }
  }

  ngAfterViewInit(): void {
  }


  onEnter(value: string) {
  }


}
