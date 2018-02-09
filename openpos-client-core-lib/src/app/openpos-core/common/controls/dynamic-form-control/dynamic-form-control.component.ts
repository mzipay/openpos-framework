import { ITextMask, TextMask } from './../../textmask';
import { IMenuItem } from '../../imenuitem';
import { IScreen } from '../../iscreen';
import { Component, ViewChild, AfterViewInit, DoCheck, OnInit, Output, Input, EventEmitter } from '@angular/core';
import { SessionService } from '../../../services/session.service';
import { MatSelectChange } from '@angular/material';
import { AbstractApp } from '../../abstract-app';
import { FormArray, FormBuilder, FormGroup, Validators, AbstractControl, FormControl, NgForm, ValidatorFn, NG_VALIDATORS } from '@angular/forms';
import { IFormElement } from '../../iformfield';
import { Observable } from 'rxjs/Observable';
import { ScreenService } from '../../../services/screen.service';

@Component({
  selector: 'app-dynamic-form-control',
  templateUrl: './dynamic-form-control.component.html',
  styleUrls: ['./dynamic-form-control.component.scss']
})
export class DynamicFormControlComponent implements OnInit {

  @Input() screenForm: IForm;

  @Input() submitAction: string;
  
  form: FormGroup;

  constructor( public session: SessionService, public screenService: ScreenService) { }

  ngOnInit() {

    this.session.screen.alternateSubmitActions.forEach(action => {
      this.session.registerActionPayload( action, () => this.session.response = this.form.value )
    });

    let group: any = {};

    this.screenForm.formElements.forEach( element => {

      let validators: ValidatorFn[] = [];
      if(element.required){
        validators.push(Validators.required);
      }
      if(element.pattern){
        validators.push(Validators.pattern(element.pattern));
      }

      group[element.id] = new FormControl(element.value, validators);
    });

    this.form = new FormGroup(group);

  }

  submitForm() {
    if (this.form.valid) {
      this.screenForm.formElements.forEach( element => {
        element.value = this.form.value[element.id];
      });

      this.session.response = this.screenForm;
      this.session.onAction(this.submitAction);
    }
  }
}

export interface IForm {
  formElements: IFormElement[];
  requiresAtLeastOneValue: Boolean;
}

