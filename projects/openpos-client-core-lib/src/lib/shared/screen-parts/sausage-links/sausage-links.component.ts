import { ScreenPartComponent } from '../screen-part';
import { IActionItem } from '../../../core/interfaces/action-item.interface';
import { Component } from '@angular/core';
import { MessageProvider } from '../../providers/message.provider';
import { Configuration } from '../../../configuration/configuration';
import { ScreenPart } from '../../decorators/screen-part.decorator';

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
