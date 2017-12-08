import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { DatePipe } from '@angular/common';
import createAutoCorrectedDatePipe from 'text-mask-addons/dist/createAutoCorrectedDatePipe';
import { AfterContentInit } from '@angular/core/src/metadata/lifecycle_hooks';

@Component({
    selector: 'app-prompt-input',
    templateUrl: './prompt-input.component.html'
})

export class PromptInputComponent implements OnInit, AfterContentInit {
    @Input() placeholderText: string;
    @Input() responseType: string;
    @Input() responseText: string;
    @Input() promptIcon: string;
    @Input() onEnterCallback: Function;
    @Input() hintText: string;
    inputType: string;
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
        if ( this.responseType === 'ALPHANUMERICPASSWORD') {
            this.inputType = 'password';
        } else {
            this.inputType = 'text';
        }
    }
    ngAfterContentInit(): void {
        if (this.responseType === 'DATE') {
            // Angular doesn't like updating the date input during ngOnInit, but it's ok with this
            if (this.responseText) {
                this.dateText = this.responseText;
            }
        }
    }


}
