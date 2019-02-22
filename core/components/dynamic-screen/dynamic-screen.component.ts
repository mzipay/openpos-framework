import { Component} from '@angular/core';
import { PersonalizationService } from '../../services';
import { MessageProvider } from '../../../shared/providers/message.provider';
@Component({
    selector: 'app-dynamic-screen',
    templateUrl: './dynamic-screen.component.html',
    styleUrls: ['./dynamic-screen.component.scss'],
    providers: [MessageProvider]
})
export class DynamicScreenComponent {

    constructor(private personalization: PersonalizationService, messageProvider: MessageProvider) {
        messageProvider.setMessageType('Screen');
    }

    getTheme() {
        return this.personalization.getTheme();
    }
}
