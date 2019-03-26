import { Component, OnInit, ViewChild, ViewChildren, QueryList } from '@angular/core';
import { FormGroup, AbstractControl } from '@angular/forms';
import { PosScreen } from '../pos-screen/pos-screen.component';
import { DynamicFormFieldComponent } from '../../shared/components/dynamic-form-field/dynamic-form-field.component';
import { ShowErrorsComponent } from '../../shared/components/show-errors/show-errors.component';
import { IForm } from '../../core/interfaces/form.interface';
import { IFormElement } from '../../core/interfaces/form-field.interface';
import { FormBuilder } from '../../core/services/form-builder.service';

@Component({
  selector: 'app-auto-complete-address',
  templateUrl: './auto-complete-address.component.html',
  styleUrls: ['./auto-complete-address.component.scss']
})
export class AutoCompleteAddressComponent extends PosScreen<any> implements OnInit {

  @ViewChildren(DynamicFormFieldComponent) children: QueryList<DynamicFormFieldComponent>;
  @ViewChild('formErrors') formErrors: ShowErrorsComponent;

  formGroup: FormGroup;
  screenForm: IForm;
  streetAddress: IFormElement;

  constructor(private formBuilder: FormBuilder) {
    super();
  }

  buildScreen() {
    this.screenForm = this.screen.form;
    this.formGroup = this.formBuilder.group(this.screenForm);

    for (const element of this.screenForm.formElements) {
      if (element.id === 'streetAddress') {
        this.streetAddress = element;
      }
    }
  }

  ngOnInit(): void {

  }

  onFieldChanged(formElement: IFormElement) {
    if (formElement.valueChangedAction) {
      this.formBuilder.buildFormPayload(this.formGroup, this.screenForm);
      this.session.onAction(formElement.valueChangedAction, this.screenForm);
    }
  }

  setAddress(address: any) {
    if (address.streetNumber) {
      this.formGroup.get('streetAddress').setValue(address.streetNumber + ' ' + address.streetName);
    } else {
      this.formGroup.get('streetAddress').setValue(address.streetName);
    }
    this.formGroup.get('locality').setValue(address.locality);
    this.formGroup.get('state').setValue(address.state);
    this.formGroup.get('postalCode').setValue(address.postalCode);
    this.formGroup.get('country').setValue(address.country);
  }

  onSubmit() {
    if (this.formGroup.valid) {
      this.formBuilder.buildFormPayload(this.formGroup, this.screenForm);
      this.onMenuItemClick(this.screen.submitButton, this.screenForm);
    } else {
      // Set focus on the first invalid field found
      const invalidFieldKey = Object.keys(this.formGroup.controls).find(key => {
        const ctrl: AbstractControl = this.formGroup.get(key);
        return ctrl.invalid && ctrl.dirty;
      });
      if (invalidFieldKey) {
        const invalidField = this.children.find(f => f.controlName === invalidFieldKey).field;
        if (invalidField) {
          const invalidElement = document.getElementById(invalidFieldKey);
          if (invalidElement) {
            invalidElement.scrollIntoView();
          } else {
            invalidField.focus();
          }
        }
      } else {
        if (this.formErrors.shouldShowErrors()) {
          const formErrorList = this.formErrors.listOfErrors();
          if (formErrorList && formErrorList.length > 0) {
            document.getElementById('formErrorsWrapper').scrollIntoView();
          }
        }
      }
    }
  }

}
