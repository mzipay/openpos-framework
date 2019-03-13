import { Component } from '@angular/core';
import { PosScreen } from '../pos-screen/pos-screen.component';
import { ScreenComponent } from '../../shared/decorators/screen.decorator';

@ScreenComponent({
    name: 'SaleRetrieval',
    moduleName: 'Core'
})
@Component({
  selector: 'app-sale-retrieval',
  templateUrl: './sale-retrieval.component.html'
})
export class SaleRetrievalComponent extends PosScreen<any> {

  buildScreen() {}

  selected(value: object) {
    this.session.onAction('Next', value);
  }
}
