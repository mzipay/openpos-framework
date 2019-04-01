import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { MessageProvider } from '../../../shared/providers/message.provider';
import { PersonalizationService } from '../../services/personalization.service';
@Component({
    selector: 'app-dynamic-screen',
    templateUrl: './dynamic-screen.component.html',
    styleUrls: ['./dynamic-screen.component.scss'],
    providers: [MessageProvider]
})
export class DynamicScreenComponent implements AfterViewInit {

    constructor(private personalization: PersonalizationService, messageProvider: MessageProvider) {
        messageProvider.setMessageType('Screen');
    }

    ngAfterViewInit() {
      }

    getTheme() {
        return this.personalization.getTheme();
    }
}
