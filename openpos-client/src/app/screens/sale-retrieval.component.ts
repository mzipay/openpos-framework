import { ISellItem } from '../common/isellitem';
import { IScreen } from '../common/iscreen';
import { IMenuItem } from '../common/imenuitem';
import {Component, ViewChild, AfterViewInit, DoCheck, OnInit} from '@angular/core';
import {SessionService} from '../services/session.service';
import { AbstractApp } from '../common/abstract-app';
import { ScanSomethingComponent } from '../common/controls/scan-something/scan-something.component';

@Component({
  selector: 'app-sale-retrieval',
  templateUrl: './sale-retrieval.component.html'
})
export class SaleRetrievalComponent implements AfterViewInit, DoCheck, IScreen, OnInit {

  constructor(public session: SessionService) {

  }
  public ngOnInit(): void {
  }

  show(session: SessionService, app: AbstractApp) {
  }

  ngDoCheck(): void {
  }

  ngAfterViewInit(): void {
  }

  selected(value: string) {
    this.session.onActionWithStringPayload('Next', value);
  }
}
