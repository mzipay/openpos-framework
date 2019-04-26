import { Component } from '@angular/core';
import { PriceCheckerHomeInterface } from './price-checker-home.interface';
import { PosScreen } from '../../screens-deprecated/pos-screen/pos-screen.component';
import { ScreenComponent } from '../../shared/decorators/screen-component.decorator';

@ScreenComponent({
    name: 'PriceCheckerHome'
})
@Component({
  selector: 'app-price-checker-home',
  templateUrl: './price-checker-home.component.html',
  styleUrls: ['./price-checker-home.component.scss']
})
export class PriceCheckerHomeComponent extends PosScreen<PriceCheckerHomeInterface> {
  buildScreen() { }
}
