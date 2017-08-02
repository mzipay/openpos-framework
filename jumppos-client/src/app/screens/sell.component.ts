import {Component, ViewChild, AfterViewInit, DoCheck} from '@angular/core';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {SessionService} from '../session.service';

@Component({
  selector: 'app-sell',
  templateUrl: './sell.component.html'
})
export class SellComponent implements AfterViewInit, DoCheck {

  @ViewChild('itemprompt') vc;

  initialized = false;

  public sellItems: ISellItem[];

  constructor(public session: SessionService) {
    this.sellItems = session.screen.items;
  }

  ngDoCheck(): void {
    console.log('ngDoCheck');
    if (this.initialized) {
      // this.vc.nativeElement.focus();
    }
  }

  ngAfterViewInit(): void {
    console.log('ngAfterViewInit');
    this.initialized = true;
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
}
