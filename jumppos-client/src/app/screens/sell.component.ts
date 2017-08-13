import { IMenuItem } from './dialog.component';
import {Component, ViewChild, AfterViewInit, DoCheck} from '@angular/core';
import {SessionService} from '../session.service';

@Component({
  selector: 'app-sell',
  templateUrl: './sell.component.html'
})
export class SellComponent implements AfterViewInit, DoCheck {

  @ViewChild('itemprompt') vc;

  initialized = false;

  public items: ISellItem[];

  constructor(public session: SessionService) {

  }

  ngDoCheck(): void {
    if (typeof this.session.screen !== 'undefined') {
      this.items = this.session.screen.items;
    }
  }

  ngAfterViewInit(): void {
    this.initialized = true;
    setTimeout(() => this.vc.nativeElement.focus(), 0);
  }

  onEnter(value: string) {
    this.session.onAction('Next');
  }

}

export interface ISellItem {
    itemId: string;
    posItemId: string;
    description: string;
    extendedAmount: number;
    quantity: number;
    lineNumber: number;
}
