import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators, ValidatorFn } from '@angular/forms';
import { IForm, IFormElement } from '../interfaces';
// cannot import the ../../shared barrel here because of a circular reference
import { OpenPosValidators } from '../../shared/validators';
import { ValidatorsService } from './validators.service';

@Injectable({
    providedIn: 'root'
})
export class FormBuilder {

    constructor( private validatorService: ValidatorsService ) {}

    group( form: IForm, extraValidators: ValidatorFn[] = []): FormGroup {
        const group: any = {};
        form.formElements.forEach(element => {
           group[element.id] = new FormControl(element.value, this.createControlValidators(element));

           // For a DATE type element, there is also a hidden field to handle picking of dates using
           // a date picker, need to add a FormControl for that hidden input also.
           if (element.inputType && element.inputType.toLowerCase().indexOf('date') >= 0) {
             group[element.id + 'Hidden'] = new FormControl();
           }
        });

        return new FormGroup(group, this.createFormLevelValidators(form, extraValidators) );
    }

    buildFormPayload( formGroup: FormGroup, form: IForm ): IForm {
       form.formElements.forEach(element => {
            if (element.hasOwnProperty('value')) {
              element.value = formGroup.value[element.id];
            }
            if (element.hasOwnProperty('checked')) {
              element.checked = (formGroup.value[element.id] === true || formGroup.value[element.id] === 'checked');
            }
          });

          return form;
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
  private createControlValidators(element: IFormElement, extraValidators: ValidatorFn[]= []): ValidatorFn[] {
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


  /**
   * Since an individual validator cannot be added after construction, this method
   * provides a way to add extra validators onto those already provided by the form.
   * A list of validators is returned which include the provided list of extraValidators.
   * The returned list of validators can then be set on the form. See the setValidators method
   * on the FormGroup class.
   *
   * @param extraValidators Optional additional validators to be added to the form.
   */
  private createFormLevelValidators( form: IForm, extraValidators: ValidatorFn[] = []): ValidatorFn[] {
    let validators: ValidatorFn[] = [];
    if (form.requiresAtLeastOneValue) {
      validators.push(OpenPosValidators.RequireAtleastOne);
    }

    validators = validators.concat(extraValidators);
    return validators;
  }

}
