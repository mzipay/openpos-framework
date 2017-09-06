import { ISellItem } from './../common/isellitem';
import { IScreen } from '../common/iscreen';
import { IMenuItem } from '../common/imenuitem';
import {Component, ViewChild, AfterViewInit, DoCheck} from '@angular/core';
import {SessionService} from '../session.service';

@Component({
  selector: 'app-sell',
  templateUrl: './sell.component.html'
})
export class SellComponent implements AfterViewInit, DoCheck, IScreen {

  @ViewChild('box') vc;

  initialized = false;

  public items: ISellItem[];

  constructor(public session: SessionService) {

  }

  show(session: SessionService) {
  }

  ngDoCheck(): void {
    if (typeof this.session.screen !== 'undefined') {
      this.items = this.session.screen.items;
    }
  }

  ngAfterViewInit(): void {
    this.initialized = true;
    // setTimeout(() => this.vc.nativeElement.focus(), 0);
  }

  onEnter(value: string) {
    this.session.onAction('Next');
  }

}
