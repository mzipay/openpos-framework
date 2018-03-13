import { FormGroup, FormControl, ValidatorFn, Validators } from '@angular/forms';
import { PromptInputComponent } from './../common/controls/prompt-input.component';
import { IScreen } from '../common/iscreen';
import { Component, ViewChild, AfterViewInit, DoCheck, OnInit } from '@angular/core';
import { SessionService } from '../services/session.service';
import { AbstractApp } from '../common/abstract-app';
import { PhoneUSValidatorDirective } from '../common/validators/phone.directive';
import { OpenPosValidators } from '../common/validators/openpos-validators';
import { ValidatorsService } from '../services/validators.service';

@Component({
  selector: 'app-prompt',
  templateUrl: './prompt.component.html'
})
export class PromptComponent implements AfterViewInit, IScreen, OnInit {
  initialized = false;
  promptFormGroup: FormGroup;

  constructor(public session: SessionService, private validatorsService: ValidatorsService) {
  }

  show(screen: any, app: AbstractApp) {
  }

  ngOnInit(): void {
    const group: any = {};
    const validators: ValidatorFn[] = [];
    validators.push(Validators.required);
    validators.push(this.validatorsService.getValidator(this.session.screen.responseType));

    if (this.session.screen.minLength) {
      validators.push(Validators.minLength(this.session.screen.minLength));
    }

    if (this.session.screen.maxLength) {
      validators.push(Validators.maxLength(this.session.screen.maxLength));
    }

    group['promptInputControl'] = new FormControl(this.session.screen.responseText, validators);
    // When showing a DATE, there is also a hidden field to handle picking of dates using
    // a date picker, need to add a FormControl for that also.
    if (this.session.screen.responseType === 'DATE') {
      group['promptInputHiddenDateControl'] = new FormControl();
    }
    this.promptFormGroup = new FormGroup(group);
  }

  ngAfterViewInit(): void {
    this.initialized = true;
  }

  onFormSubmit(): void {
    if (this.promptFormGroup.valid) {
      const payload = this.promptFormGroup.value['promptInputControl'];
      if (this.session.screen.actionButton) {
        this.session.onAction(this.session.screen.actionButton.action, payload);
      } else {
        this.session.onAction(this.session.screen.action, payload);
      }
    }
  }

}
