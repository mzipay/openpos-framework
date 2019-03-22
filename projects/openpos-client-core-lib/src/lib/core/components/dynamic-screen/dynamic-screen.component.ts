import { AfterViewInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { ViewChild } from '@angular/core';
import { Component } from '@angular/core';
import { PersonalizationService } from '../../services';
import { MessageProvider } from '../../../shared/providers/message.provider';
@Component({
    selector: 'app-dynamic-screen',
    templateUrl: './dynamic-screen.component.html',
    styleUrls: ['./dynamic-screen.component.scss'],
    providers: [MessageProvider]
})
export class DynamicScreenComponent implements AfterViewInit {

    @ViewChild('focusArea') focusArea: ElementRef;

    constructor(private personalization: PersonalizationService, messageProvider: MessageProvider) {
        messageProvider.setMessageType('Screen');
    }

    ngAfterViewInit() {
        this.focusArea.nativeElement.focus();
      }

    getTheme() {
        return this.personalization.getTheme();
    }
}
