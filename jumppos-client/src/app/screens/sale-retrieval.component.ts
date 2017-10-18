import { ISellItem } from '../common/isellitem';
import { IScreen } from '../common/iscreen';
import { IMenuItem } from '../common/imenuitem';
import {Component, ViewChild, AfterViewInit, DoCheck} from '@angular/core';
import {SessionService} from '../session.service';

@Component({
  selector: 'app-sale-retrieval',
  templateUrl: './sale-retrieval.component.html'
})
export class SaleRetrievalComponent implements AfterViewInit, DoCheck, IScreen {

  constructor(public session: SessionService) {

  }

  show(session: SessionService) {
  }

  ngDoCheck(): void {
  }

  ngAfterViewInit(): void {
  }

  selected(value: string) {
    this.session.onActionWithStringPayload('Next', value);
  }

}
