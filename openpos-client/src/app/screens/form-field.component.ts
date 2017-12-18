import { Component, OnInit, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { IFormElement } from '../common/iformfield';

@Component({
  selector: 'app-form-field',
  templateUrl: './form-field.component.html'
})
export class FormFieldComponent implements OnInit {

  @Input() formField: IFormElement;
  @Input() formGroup: FormGroup;

  constructor() { }

  ngOnInit() {
  }

  get isValid() { return this.formGroup.controls[this.formField.id].valid; }

  getErrorMessage() {
    // return this.email.hasError('required') ? 'You must enter a value' :
    //     this.email.hasError('email') ? 'Not a valid email' :
    //         '';
    return 'Error message test';
  }

}
