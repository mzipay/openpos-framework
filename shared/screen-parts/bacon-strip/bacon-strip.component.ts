import { Component } from '@angular/core';
import { BaconStripInterface } from './bacon-strip.interface';
import { ScreenPart, ScreenPartData } from '../screen-part';

@ScreenPartData({name: 'baconStrip'})
@Component({
    selector: 'app-bacon-strip',
    templateUrl: './bacon-strip.component.html',
    styleUrls: ['./bacon-strip.component.scss']
})
export class BaconStripComponent extends ScreenPart<BaconStripInterface> {

    screenDataUpdated() {
    }
}
