import { DialogService } from './../../services/dialog.service';
import { FocusService } from './../../services/focus.service';
import { AfterViewInit, Component, ElementRef, ViewChild, AfterViewChecked } from '@angular/core';
import { MessageProvider } from '../../../shared/providers/message.provider';
import { PersonalizationService } from '../../services/personalization.service';
@Component({
    selector: 'app-dynamic-screen',
    templateUrl: './dynamic-screen.component.html',
    styleUrls: ['./dynamic-screen.component.scss'],
    providers: [MessageProvider]
})
export class DynamicScreenComponent implements AfterViewInit, AfterViewChecked {

    @ViewChild('focusArea') focusArea: ElementRef;

    constructor(private personalization: PersonalizationService,
                private dialogService: DialogService,
                messageProvider: MessageProvider, private focusService: FocusService) {
        messageProvider.setMessageType('Screen');
    }

    ngAfterViewInit() {
    }

    ngAfterViewChecked(): void {
        if (!this.dialogService.isDialogOpenOrOpening()) {
            this.focusService.requestFocus('hidden-div', this.focusArea.nativeElement);
        }
    }


    getTheme() {
        return this.personalization.getTheme();
    }
}
