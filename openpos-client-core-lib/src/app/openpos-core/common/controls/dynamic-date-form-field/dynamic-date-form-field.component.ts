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
  
    @Input() formField: IFormElement;
    @Input() formGroup: FormGroup;
  
    dateMask = [/\d/, /\d/, '/', /\d/, /\d/,'/', /\d/, /\d/, /\d/, /\d/];
    autoCorrectedDatePipe = createAutoCorrectedDatePipe('mm/dd/yyyy');
    format = 'MM/dd/yyyy';
    public keyboardLayout = 'en-US';

    @Output() onFieldChanged = new EventEmitter<IFormElement>();
  
    public values: Array<string> = [];
  
    constructor(@Optional() private datePipe: DatePipe) {}
  
    ngOnInit() {
        if(this.formField.inputType === 'BirthDate') {
            this.dateMask = [/\d/, /\d/, '/', /\d/, /\d/];
            this.autoCorrectedDatePipe = createAutoCorrectedDatePipe('mm/dd');
            this.format = 'MM/dd';
        }
    }
    public onDateEntered(): void {
      if (this.formField.value) {
        this.formField.value = this.formField.value.replace(/_/g, '');
      }
    }
  
    public onDatePicked(event: MatDatepickerInputEvent<Date>): void {
      this.formField.value = this.datePipe.transform(event.value, this.format);
      this.formGroup.get(this.formField.id).setValue(this.formField.value);
    }
  
    getPlaceholderText(formElement: IFormElement) {
        let text = '';
        if (formElement.label) {
          text += formElement.label;
        }
        if (text && formElement.placeholder) {
          text = `${text} - ${formElement.placeholder}`;
        }
    
        return text;
      }
  }
  
  