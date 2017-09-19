import { Component, Input, OnInit } from '@angular/core';

@Component({
    selector: 'app-prompt-input',
    templateUrl: './prompt-input.component.html'
})

export class PromptInputComponent {
    @Input() placeholderText: string;
    @Input() responseType: string;
    @Input() responseText: string;
    @Input() promptIcon: string;
    @Input() onEnterCallback: Function;

    public onEnter($event): void {
        this.onEnterCallback($event, this);
    }

}
