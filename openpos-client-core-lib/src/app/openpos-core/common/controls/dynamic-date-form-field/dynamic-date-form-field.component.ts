import { Component, OnInit, Input, Output, EventEmitter, Optional } from "@angular/core";
import createAutoCorrectedDatePipe from "text-mask-addons/dist/createAutoCorrectedDatePipe";
import { IFormElement } from "../../iformfield";
import { FormGroup } from "@angular/forms";
import { DatePipe } from "@angular/common";
import { MatSelectChange, MatDatepickerInputEvent } from "@angular/material";


@Component({
    selector: 'app-dynamic-date-form-field',
    templateUrl: './dynamic-date-form-field.component.html',
    styleUrls: ['./dynamic-date-form-field.component.scss']
  })
  export class DynamicDateFormFieldComponent implements OnInit {
  
    @Input() type: string;
    @Input() value: string;
    @Input() placeholder: string;
    @Input() isPrompt: boolean = false;
    @Input() hintText: string ='';
    @Input() controlName: string;
    @Input() form: FormGroup;

    @Output() valueChange = new EventEmitter<any>();
  
    dateMask = [/\d/, /\d/, '/', /\d/, /\d/,'/', /\d/, /\d/, /\d/, /\d/];
    autoCorrectedDatePipe = createAutoCorrectedDatePipe('mm/dd/yyyy');
    format = 'MM/dd/yyyy';
  
    constructor(@Optional() private datePipe: DatePipe) {}
  
    ngOnInit() {
        if(this.type === 'NoYearDate' || this.type === 'NOYEARDATE') {
            this.dateMask = [/\d/, /\d/, '/', /\d/, /\d/];
            this.autoCorrectedDatePipe = createAutoCorrectedDatePipe('mm/dd');
            this.format = 'MM/dd';
        }
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
  
  }
  
  