import { ScreenPartComponent, ScreenPart } from '../screen-part';
import { IActionItem } from '../../../core/interfaces/menu-item.interface';
import { Component } from '@angular/core';
import { MessageProvider } from '../../providers/message.provider';
import { Configuration } from '../../../configuration/configuration';

@ScreenPart({
    name: 'sausageLinks'
})
@Component({
    selector: 'app-sausage-links',
    templateUrl: './sausage-links.component.html',
    styleUrls: ['./sausage-links.component.scss']
})
export class SausageLinksComponent extends ScreenPartComponent<IActionItem[]> {

    links: IActionItem[];

    constructor( messageProvider: MessageProvider ) {
        super(messageProvider);
    }

    screenDataUpdated() {
        if ( Array.isArray(this.screenData)) {
            this.links = this.screenData;
        }
    }

    keybindsEnabled() {
        return Configuration.enableKeybinds;
    }
}
