import { Component, Input, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import createAutoCorrectedDatePipe from 'text-mask-addons/dist/createAutoCorrectedDatePipe';

@Component({
    selector: 'app-prompt-input',
    templateUrl: './prompt-input.component.html'
})

export class PromptInputComponent implements OnInit {
    @Input() placeholderText: string;
    @Input() responseType: string;
    @Input() responseText: string;
    @Input() promptIcon: string;
    @Input() onEnterCallback: Function;
    dateText: string;  // value entered by user or copied from datePickerValue
    datePickerValue: Date;  // retains value picked using datepicker
    // Configuration for date masking
    dateMask = [/\d/, /\d/, '/', /\d/, /\d/, '/', /\d/, /\d/, /\d/, /\d/];
    autoCorrectedDatePipe = createAutoCorrectedDatePipe('mm/dd/yyyy');

    constructor(private datePipe: DatePipe) {}

    public onEnter($event): void {
        this.onEnterCallback($event, this);
    }

    public onDateEntered($event): void {
        if (this.dateText) {
            this.dateText = this.dateText.replace(/_/g, '');
            if (this.dateText.length === 10) {
               this.responseText = this.dateText;
            }
        }
        this.onEnterCallback($event, this);
    }

    public onDatePicked($event): void {
        this.dateText = this.datePipe.transform(this.datePickerValue, 'MM/dd/yyyy');
    }

    ngOnInit(): void {
    }
}
