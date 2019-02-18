import { Component } from '@angular/core';
import { ScreenPart, ScreenPartData } from '../screen-part';
import { IActionItem } from '../../../core/interfaces/menu-item.interface';

@ScreenPartData({name: 'sausageLinks'})
@Component({
    selector: 'app-sausage-links',
    templateUrl: './sausage-links.component.html',
    styleUrls: ['./sausage-links.component.scss']
})
export class SausageLinksComponent extends ScreenPart<IActionItem[]> {

    links: IActionItem[];

    screenDataUpdated() {
        if ( Array.isArray(this.screenData)) {
            this.links = this.screenData;
        }
    }
}
