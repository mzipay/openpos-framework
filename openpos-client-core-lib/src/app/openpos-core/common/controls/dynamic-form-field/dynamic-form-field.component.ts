import { ITextMask, TextMask } from './../../textmask';
import { IMenuItem } from '../../imenuitem';
import { IScreen } from '../../iscreen';
import { Component, ViewChild, AfterViewInit, DoCheck, OnInit, Output, Input, EventEmitter, Optional, ElementRef } from '@angular/core';
import { SessionService } from '../../../services/session.service';
import { MatSelectChange, MatDatepickerInputEvent } from '@angular/material';
import { AbstractApp } from '../../abstract-app';
import { FormArray, FormBuilder, FormGroup, Validators, AbstractControl, FormControl, NgForm } from '@angular/forms';
import { IFormElement } from '../../iformfield';
import { Observable } from 'rxjs/Observable';
import { ScreenService } from '../../../services/screen.service';
import { DatePipe } from '@angular/common';
import createAutoCorrectedDatePipe from 'text-mask-addons/dist/createAutoCorrectedDatePipe';

@Component({
  selector: 'app-dynamic-form-field',
  templateUrl: './dynamic-form-field.component.html',
  styleUrls: ['./dynamic-form-field.component.scss']
})
export class DynamicFormFieldComponent implements OnInit {

  @Input() formField: IFormElement;
  @Input() formGroup: FormGroup;

  dateMask = [/\d/, /\d/, '/', /\d/, /\d/, '/', /\d/, /\d/, /\d/, /\d/];
  autoCorrectedDatePipe = createAutoCorrectedDatePipe('mm/dd/yyyy');

  // tslint:disable-next-line:no-output-on-prefix
  @Output() onFieldChanged = new EventEmitter<IFormElement>();

  public values: Observable<String[]>;

  constructor(public session: SessionService, public screenService: ScreenService, @Optional() private datePipe: DatePipe) { }

  ngOnInit() {
    if (this.formField.inputType === 'ComboBox' ||
      this.formField.inputType === 'SubmitOptionList' ||
      this.formField.inputType === 'ToggleButton') {
      this.values = this.screenService.getFieldValues(this.formField.id);
    }
  }

  onFormElementChanged(formElement: IFormElement): void {
    this.onFieldChanged.emit(formElement);
  }

  getFormFieldMask(): ITextMask {
    if (this.formField.mask) {
      return TextMask.instance(this.formField.mask);
    } else {
      return TextMask.NO_MASK;
    }
  }

  public onDateEntered(): void {
    if (this.formField.value) {
      this.formField.value = this.formField.value.replace(/_/g, '');
    }
  }

  public onDatePicked(event: MatDatepickerInputEvent<Date>): void {
    this.formField.value = this.datePipe.transform(event.value, 'MM/dd/yyyy');
    this.formGroup.get(this.formField.id).setValue(this.formField.value);
  }


  onSubmitOptionSelected(formElement: IFormElement, valueIndex: number, event: Event) {
    if (formElement.selectedIndexes) {
      formElement.selectedIndexes = [valueIndex];
    }

    // this.session.response = this.screenForm;
    // this.session.onAction(formElement.id);
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

