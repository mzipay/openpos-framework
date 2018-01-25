import { ITextMask, TextMask } from './../../textmask';
import { IMenuItem } from '../../imenuitem';
import { IScreen } from '../../iscreen';
import { Component, ViewChild, AfterViewInit, DoCheck, OnInit, Output, Input, EventEmitter } from '@angular/core';
import { SessionService } from '../../../services/session.service';
import { MatSelectChange } from '@angular/material';
import { AbstractApp } from '../../abstract-app';
import { FormArray, FormBuilder, FormGroup, Validators, AbstractControl, FormControl, NgForm } from '@angular/forms';
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
  @Input() formField: IFormElement;

  @Input() submitAction: string;
  @Output() onFieldChanged = new EventEmitter<{ formElement: IFormElement, event: Event }>();
  @ViewChild('form') form: NgForm;

  public values: Observable<String[]>

  constructor( public session: SessionService, public screenService: ScreenService) { }

  ngOnInit() {
    this.values = this.screenService.getFieldValues(this.formField.id);

    this.session.screen.alternateSubmitActions.forEach(action => {
      this.session.registerActionPayload( action, () => this.session.response = this.screenForm )
    });
  }

  onFormElementChanged(formElement: IFormElement, event: Event): void {
    this.onFieldChanged.emit({ formElement: formElement, event: event });
    if (formElement.inputType === 'ComboBox' && formElement.valueChangedAction) {
      this.session.response = this.screenForm;
      this.session.onAction(formElement.valueChangedAction);
    }
  }

  onSubmitOptionSelected(formElement: IFormElement, valueIndex: number, event: Event) {
    if (formElement.selectedIndexes) {
      formElement.selectedIndexes = [valueIndex];
    }

    this.session.response = this.screenForm;
    this.session.onAction(formElement.id);
  }

  getFormFieldMask(formElement: IFormElement): ITextMask {
    if (formElement.mask) {
      return TextMask.instance(formElement.mask);
    } else  {
      return TextMask.NO_MASK;
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
  requiresAtLeastOneValue: Boolean;
}

