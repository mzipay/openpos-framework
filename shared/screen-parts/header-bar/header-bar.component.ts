import { Component } from '@angular/core';
import { ScreenPart } from '../screen-part';
import { HeaderBarInterface } from './header-bar.interface';

@Component({
    selector: 'app-header-bar',
    templateUrl: './header-bar.component.html',
    styleUrls: ['./header-bar.component.scss']
})
export class HeaderBarComponent extends ScreenPart<HeaderBarInterface> {

    screenDataUpdated() {
    }
}
