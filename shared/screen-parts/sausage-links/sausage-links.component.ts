import { ScreenPartComponent, ScreenPart } from '../screen-part';
import { IActionItem } from '../../../core/interfaces/menu-item.interface';

@ScreenPart({
    selector: 'app-sausage-links',
    templateUrl: './sausage-links.component.html',
    styleUrls: ['./sausage-links.component.scss'],
    name: 'sausageLinks'
})
export class SausageLinksComponent extends ScreenPartComponent<IActionItem[]> {

    links: IActionItem[];

    screenDataUpdated() {
        if ( Array.isArray(this.screenData)) {
            this.links = this.screenData;
        }
    }
}
