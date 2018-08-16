import { Component, OnInit, Input, Output, EventEmitter, Optional, ViewChild } from '@angular/core';
import createAutoCorrectedDatePipe from 'text-mask-addons/dist/createAutoCorrectedDatePipe';
import { FormGroup } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { MatDatepickerInputEvent, MatInput } from '@angular/material';

@Component({
    selector: 'app-dynamic-date-form-field',
    templateUrl: './dynamic-date-form-field.component.html',
    styleUrls: ['./dynamic-date-form-field.component.scss']
  })
  export class DynamicDateFormFieldComponent implements OnInit {
    protected static readonly DEFAULT_MASK = [/\d/, /\d/, '/', /\d/, /\d/, '/', /\d/, /\d/, /\d/, /\d/];
    protected static readonly dateMasks: Map<string, DateFormatEntry> = new Map([
        ['date', { mask: DynamicDateFormFieldComponent.DEFAULT_MASK, format: 'MM/dd/yyyy',
                   datePipe: createAutoCorrectedDatePipe('mm/dd/yyyy') }],
        ['noyeardate', { mask: [/\d/, /\d/, '/', /\d/, /\d/], format: 'MM/dd',
                         datePipe: createAutoCorrectedDatePipe('mm/dd') }],
        ['datemmddyy', { mask: [/\d/, /\d/, '/', /\d/, /\d/, '/', /\d/, /\d/], format: 'MM/dd/yy',
                         datePipe: createAutoCorrectedDatePipe('mm/dd/yy') }]
    ]);

    @ViewChild(MatInput) field: MatInput;

    @Input() type: string;
    @Input() value: string;
    @Input() placeholder: string;
    @Input() isPrompt = false;
    @Input() hideCalendar = false;
    @Input() hintText = '';
    @Input() controlName: string;
    @Input() hiddenControl: string;
    @Input() form: FormGroup;

    @Output() valueChange = new EventEmitter<any>();

    dateMask = DynamicDateFormFieldComponent.dateMasks.get('date').mask; // [/\d/, /\d/, '/', /\d/, /\d/,'/', /\d/, /\d/, /\d/, /\d/];
    autoCorrectedDatePipe = DynamicDateFormFieldComponent.dateMasks.get('date').datePipe;
    format = DynamicDateFormFieldComponent.dateMasks.get('date').format;
    dateValue: Date;

    constructor(@Optional() private datePipe: DatePipe) {
    }

    ngOnInit() {
        if (this.type) {
            const lowerType = this.type.toLowerCase();
            this.dateMask = DynamicDateFormFieldComponent.dateMasks.get(lowerType).mask;
            this.autoCorrectedDatePipe = DynamicDateFormFieldComponent.dateMasks.get(lowerType).datePipe;
            this.format = DynamicDateFormFieldComponent.dateMasks.get(lowerType).format;

        }
    }

    public focus(): void {
        this.field.focus();
    }

    public onDateEntered(): void {
      if (this.value) {
        this.value = this.value.replace(/_/g, '');
        this.valueChange.emit(this.value);
      }
    }

    public onDatePicked(event: MatDatepickerInputEvent<Date>): void {
      this.value = this.datePipe.transform(event.value, this.format);
      this.form.get(this.controlName).setValue(this.value);
      this.valueChange.emit(this.value);
    }

    public onDateChange(): void {
      const dates = this.value.split('/');
      if (dates.length > 1) {
        // JavaScript counts months from 0 to 11. January is 0. December is 11.
        const month = parseInt(dates[0], 10) - 1;
        const day = parseInt(dates[1], 10);
        const year = (new Date()).getFullYear;
        if (dates.length > 2) {
          const y = parseInt(dates[2], 10);
          this.dateValue = new Date(y, month, day, 0, 0, 0, 0);
        } else {
          const todayYear = (new Date()).getFullYear();
          this.dateValue = new Date(todayYear, month, day, 0, 0, 0, 0);
        }
      }
    }

  }

  interface DateFormatEntry {
      mask: Array<string|RegExp>;
      format: string;
      datePipe: any;
  }
