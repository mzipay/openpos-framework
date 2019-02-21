import { ScreenPartComponent, ScreenPart } from '../screen-part';
import { IActionItem } from '../../../core/interfaces/menu-item.interface';
import { Component, forwardRef } from '@angular/core';

@ScreenPart({
    name: 'sausageLinks'
})
@Component({
    selector: 'app-sausage-links',
    templateUrl: './sausage-links.component.html',
    styleUrls: ['./sausage-links.component.scss'],
    providers: [{provide: ScreenPartComponent, useExisting: forwardRef( () => SausageLinksComponent )}]
})
export class SausageLinksComponent extends ScreenPartComponent<IActionItem[]> {

    links: IActionItem[];

    screenDataUpdated() {
        if ( Array.isArray(this.screenData)) {
            this.links = this.screenData;
        }
    }
}
