import { ScreenPart } from '../../decorators/screen-part.decorator';
import { Component } from '@angular/core';
import { ScreenPartComponent } from '../screen-part';
import { DialogHeaderInterface } from './dialog-header.interface';
import { MessageProvider } from '../../providers/message.provider';

@ScreenPart({
    name: 'dialogHeader'})
@Component({
    selector: 'app-dialog-header',
    templateUrl: './dialog-header.component.html',
    styleUrls: ['./dialog-header.component.scss']
})
export class DialogHeaderComponent extends ScreenPartComponent<DialogHeaderInterface> {

    constructor( messageProvider: MessageProvider) {
        super(messageProvider);
    }

    screenDataUpdated() {
        if (this.screenData.backButton) {
            this.screenData.backButton.keybind = 'Escape';
        }
    }
}
