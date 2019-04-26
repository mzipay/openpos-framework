import { Component } from '@angular/core';
import { PosScreen } from '../../screens-deprecated/pos-screen/pos-screen.component';
import { ScreenComponent } from '../../shared/decorators/screen-component.decorator';
import { PriceCheckerItemDetailInterface } from './price-checker-item-detail.interface';

@ScreenComponent({
    name: 'PriceCheckerItemDetail'
})
@Component({
    selector: 'app-price-checker-item-detail',
    templateUrl: 'price-checker-item-detail.component.html',
    styleUrls: ['./price-checker-item-detail.component.scss']
})
export class PriceCheckerItemDetailComponent extends PosScreen<PriceCheckerItemDetailInterface> {

    buildScreen() {}

}
