import { ITextMask, TextMask } from './../../textmask';
import { IMenuItem } from '../../imenuitem';
import { IScreen } from '../../iscreen';
import { Component, ViewChild, AfterViewInit, DoCheck, OnInit, Output, Input, EventEmitter } from '@angular/core';
import { SessionService } from '../../../services/session.service';
import { MatSelectChange } from '@angular/material';
import { AbstractApp } from '../../abstract-app';
import {
  FormArray, FormBuilder, FormGroup, Validators, AbstractControl,
  FormControl, NgForm, ValidatorFn, NG_VALIDATORS
} from '@angular/forms';
import { IFormElement } from '../../iformfield';
import { Observable } from 'rxjs/Observable';
import { ScreenService } from '../../../services/screen.service';
import { OpenPosValidators } from '../../validators/openpos-validators';
import { ValidatorsService } from '../../../services/validators.service';
import { IForm } from '../../iform';

@Component({
  selector: 'app-dynamic-form-control',
  templateUrl: './dynamic-form-control.component.html',
  styleUrls: ['./dynamic-form-control.component.scss']
})
export class DynamicFormControlComponent implements OnInit {

  @Input() screenForm: IForm;

  @Input() submitAction: string;

  @Input() submitButtonText = 'Next';

  @Input() screen: any;

  form: FormGroup;

  constructor(public session: SessionService, public screenService: ScreenService, private validatorService: ValidatorsService) { }

  ngOnInit() {

    if (this.screen.alternateSubmitActions) {
      this.screen.alternateSubmitActions.forEach(action => {

        this.session.registerActionPayload(action, () => {
          if (this.form.valid) {
              this.buildFormPayload();
              return this.session.response = this.screenForm;
          } else {
              throw Error('form is invalid');
          }
        });
      });
    }

    const group: any = {};

    this.screenForm.formElements.forEach(element => {

      const ctlValidators: ValidatorFn[] = this.createControlValidators(element);
      group[element.id] = new FormControl(element.value, ctlValidators);
      // For a DATE type element, there is also a hidden field to handle picking of dates using
      // a date picker, need to add a FormControl for that hidden input also.
      if(element.inputType === 'Date' || element.inputType === 'NoYearDate') {
        group[element.id+'Hidden'] = new FormControl();
      }
    });

    const grpValidators: ValidatorFn[] = this.createFormLevelValidators();
    this.form = new FormGroup(group, grpValidators);

  }

  /**
   * Since an individual validator cannot be added after construction, this method
   * provides a way to add extra validators onto those already provided by the form.
   * A list of validators is returned which include the provided list of extraValidators.
   * The returned list of validators can then be set on the form. See the setValidators method
   * on the FormGroup class.
   *
   * @param extraValidators Optional additional validators to be added to the form.
   */
  createFormLevelValidators(extraValidators: ValidatorFn[] = []): ValidatorFn[] {
    let validators: ValidatorFn[] = [];
    if (this.screenForm.requiresAtLeastOneValue) {
      validators.push(OpenPosValidators.RequireAtleastOne);
    }

    validators = validators.concat(extraValidators);
    return validators;
  }

  /**
   * Since an individual validator cannot be added after construction, this method
   * provides a way to add extra validators onto those already normally assigned to the
   * IFormElement.
   * A list of validators is returned which include the provided list of extraValidators.
   * The returned list of validators can then be set on the form. See the setValidators method
   * on the FormComponent class.
   *
   * @param extraValidators Optional additional validators to be added to the form.
   */
  createControlValidators(element: IFormElement, extraValidators: ValidatorFn[] = []): ValidatorFn[] {
    let validators: ValidatorFn[] = [];
    if (element.required) {
      validators.push(Validators.required);
    }

    if (element.pattern) {
      validators.push(Validators.pattern(element.pattern));
    }

    if (element.minLength) {
      validators.push(Validators.minLength(element.minLength));
    }

    if (element.maxLength) {
      validators.push(Validators.maxLength(element.maxLength));
    }

    validators.push(this.validatorService.getValidator(element.inputType));

    validators = validators.concat(extraValidators);
    return validators;
  }

  submitForm() {
    if (this.form.valid) {

      this.buildFormPayload();
      this.session.onAction(this.submitAction, this.screenForm);
    }
  }

  onFieldChanged(formElement: IFormElement) {
    if (formElement.valueChangedAction) {
      this.buildFormPayload();
      this.session.onAction(formElement.valueChangedAction, this.screenForm);
    }
  }

  private buildFormPayload() {
    this.screenForm.formElements.forEach(element => {
      if (element.hasOwnProperty('value')) {
        element.value = this.form.value[element.id];
      }
    });
  }
}

