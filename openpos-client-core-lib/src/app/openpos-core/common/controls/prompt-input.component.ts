import { Component, Input, OnInit, ViewChild, Output, EventEmitter } from '@angular/core';
import { DatePipe } from '@angular/common';
import createAutoCorrectedDatePipe from 'text-mask-addons/dist/createAutoCorrectedDatePipe';
import { AfterContentInit } from '@angular/core/src/metadata/lifecycle_hooks';
import { TextMask, IMaskSpec, ITextMask } from '../textmask';

@Component({
    selector: 'app-prompt-input',
    templateUrl: './prompt-input.component.html'
})

export class PromptInputComponent implements OnInit, AfterContentInit {
    @Input() placeholderText: string;
    @Input() responseType: string;
    @Input() responseText: string;
    @Input() promptIcon: string;
    @Input() hintText: string;
    @Input() maskSpec: IMaskSpec;
    @Output() enter = new EventEmitter<string>();
    inputType: string;
    dateText: string;  // value entered by user or copied from datePickerValue
    datePickerValue: Date;  // retains value picked using datepicker
    // Configuration for date masking
    dateMask = [/\d/, /\d/, '/', /\d/, /\d/, '/', /\d/, /\d/, /\d/, /\d/];
    autoCorrectedDatePipe = createAutoCorrectedDatePipe('mm/dd/yyyy');

    onOffModel: boolean;
    _textMask: ITextMask; // Mask object built for text-mask

    constructor(private datePipe: DatePipe) {}

    public onEnter(): void {
        this.enter.emit(this.responseText);
    }

    public onSlideChange(): void {
      if( this.responseType === "ONOFF" )
        this.responseText = this.onOffModel ? "ON" : "OFF";
    }

    public onDateEntered(): void {
        if (this.dateText) {
            this.dateText = this.dateText.replace(/_/g, '');
            if (this.dateText.length === 10) {
               this.responseText = this.dateText;
            }
        }
        this.enter.emit(this.responseText);
    }

    public onDatePicked(): void {
        this.dateText = this.datePipe.transform(this.datePickerValue, 'MM/dd/yyyy');
    }

    ngOnInit(): void {
        if ( this.responseType === 'ALPHANUMERICPASSWORD') {
            this.inputType = 'password';
        } else {
            this.inputType = 'text';
        }
        if (this.maskSpec) {
            const newMask = TextMask.instance(this.maskSpec);
            this._textMask = newMask;
        }

        if ( this.responseType === "ONOFF" ){
          if( !this.responseText ) {
            this.responseText = "OFF";
          }
          this.onOffModel = this.responseText === "ON";
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
