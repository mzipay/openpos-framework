import { Component } from '@angular/core';
import { BaconStripInterface } from './bacon-strip.interface';
import { ScreenPart, ScreenPartData } from '../screen-part';
import { OpenposMediaService } from '../../../core';
import { Observable } from 'rxjs';

@ScreenPartData({name: 'baconStrip'})
@Component({
    selector: 'app-bacon-strip',
    templateUrl: './bacon-strip.component.html',
    styleUrls: ['./bacon-strip.component.scss']
})
export class BaconStripComponent extends ScreenPart<BaconStripInterface> {

    isMobile$: Observable<boolean>;

    constructor( mediaService: OpenposMediaService) {
        super();
        const mobileMap = new Map([
            ['xs', true],
            ['sm', false],
            ['md', false],
            ['lg', false],
            ['xl', false]
        ]);
        this.isMobile$ = mediaService.mediaObservableFromMap(mobileMap);
    }

    screenDataUpdated() {
    }
}
