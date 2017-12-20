import { IMenuItem } from './../../common/imenuitem';
import { IScreen } from '../../common/iscreen';
import { Component, ViewChild, AfterViewInit, DoCheck, OnInit, Output, Input, EventEmitter } from '@angular/core';
import { SessionService } from '../../services/session.service';
import { MatSelectChange } from '@angular/material';
import { AbstractApp } from '../../common/abstract-app';
import { FormArray, FormBuilder, FormGroup, Validators, AbstractControl, FormControl, NgForm } from '@angular/forms';
import { IFormElement } from '../../common/iformfield';


@Component({
  selector: 'app-dynamic-form',
  templateUrl: './dynamic-form.component.html'
})
export class DynamicFormComponent implements IScreen, OnInit {

  public screenForm: IForm;
  @Output() onFieldChanged = new EventEmitter<{ formElement: IFormElement, event: Event }>();
  @Input() formFields: IFormElement[];

  constructor(public session: SessionService, public formBuilder: FormBuilder) {
  }

  show(session: SessionService, app: AbstractApp) {
  }

  ngOnInit(): void {
    this.screenForm = this.session.screen.form;
    this.formFields = this.session.screen.form.formFields;
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

  submitForm(form: NgForm) {
    if (form.valid) {
      // could submit form.value instead which is simple name value pairs
      this.session.response = this.screenForm;
      this.session.onAction(this.session.screen.submitAction);
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

}

export interface IForm {
  formElements: IFormElement[];
}
