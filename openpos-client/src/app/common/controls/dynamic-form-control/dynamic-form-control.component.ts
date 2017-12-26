import { IMenuItem } from '../../imenuitem';
import { IScreen } from '../../iscreen';
import { Component, ViewChild, AfterViewInit, DoCheck, OnInit, Output, Input, EventEmitter } from '@angular/core';
import { SessionService } from '../../../services/session.service';
import { MatSelectChange } from '@angular/material';
import { AbstractApp } from '../../abstract-app';
import { FormArray, FormBuilder, FormGroup, Validators, AbstractControl, FormControl, NgForm } from '@angular/forms';
import { IFormElement } from '../../iformfield';

@Component({
  selector: 'app-dynamic-form-control',
  templateUrl: './dynamic-form-control.component.html',
  styleUrls: ['./dynamic-form-control.component.scss']
})
export class DynamicFormControlComponent implements OnInit {

  @Input() screenForm: IForm;
  @Input() submitAction: string;
  @Output() onFieldChanged = new EventEmitter<{ formElement: IFormElement, event: Event }>();
  @ViewChild('form') form: NgForm;

  constructor( private session: SessionService) { }

  ngOnInit() {
  }

  onFormElementChanged(formElement: IFormElement, event: Event): void {
    this.onFieldChanged.emit({ formElement: formElement, event: event });
  }

  onSubmitOptionSelected(formElement: IFormElement, valueIndex: number, event: Event) {
    if (formElement.selectedIndexes) {
      formElement.selectedIndexes = [valueIndex];
    }

    this.session.response = this.screenForm;
    this.session.onAction(formElement.id);
  }

  submitForm() {
    if (this.form.valid && !this.requiresAtLeastOneField()) {
      // could submit form.value instead which is simple name value pairs
      this.session.response = this.screenForm;
      this.session.onAction(this.submitAction);
    }
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

  requiresAtLeastOneField(): Boolean {
    if (this.screenForm.requiresAtLeastOneValue) {
      const value = this.form.value;
      for (const key in value) {
        if (value[key]) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }
}

export interface IForm {
  formElements: IFormElement[];
  requiresAtLeastOneValue: Boolean;
}

