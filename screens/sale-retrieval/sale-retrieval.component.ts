import { Component } from '@angular/core';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
  selector: 'app-sale-retrieval',
  templateUrl: './sale-retrieval.component.html'
})
export class SaleRetrievalComponent extends PosScreen<any> {

  constructor() {
    super();
  }

  buildScreen(){}

  selected(value: string) {
    this.session.onAction('Next', value);
  }
}
