import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { PosScreen } from '../pos-screen/pos-screen.component';


@Component({
  selector: 'app-auto-complete-address',
  templateUrl: './auto-complete-address.component.html',
  styleUrls: ['./auto-complete-address.component.scss']
})
export class AutoCompleteAddressComponent extends PosScreen<any> implements OnInit {

  formGroup: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    super();
  }

  buildScreen() {
  }

  ngOnInit(): void {
    this.formGroup = this.formBuilder.group({
      streetAddress: ['', [Validators.required]],
      addressLine2: ['', []],
      locality: ['', [Validators.required]],
      state: ['', [Validators.required]],
      postalCode: ['', [Validators.required]],
      country: ['', [Validators.required]],
    });
  }

  setAddress(address: any) {
    this.formGroup.get('streetAddress').setValue(address.streetNumber + ' ' + address.streetName);
    this.formGroup.get('locality').setValue(address.locality);
    this.formGroup.get('state').setValue(address.state);
    this.formGroup.get('postalCode').setValue(address.postalCode);
    this.formGroup.get('country').setValue(address.country);
  }

}
