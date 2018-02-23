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
  selector: 'app-dynamic-form-field',
  templateUrl: './dynamic-form-field.component.html',
  styleUrls: ['./dynamic-form-field.component.scss']
})
export class DynamicFormFieldComponent implements OnInit {

  @Input() formField: IFormElement;
  @Input() formGroup: FormGroup;

  @Output() onFieldChanged = new EventEmitter<IFormElement>();
 
  public values: Observable<String[]>

  constructor( public session: SessionService, public screenService: ScreenService) { }

  ngOnInit() {
    this.values = this.screenService.getFieldValues(this.formField.id);
  }

  onFormElementChanged(formElement: IFormElement): void {
    this.onFieldChanged.emit(formElement);
  }

  getFormFieldMask(): ITextMask {
    if (this.formField.mask) {
      return TextMask.instance(this.formField.mask);
    } else  {
      return TextMask.NO_MASK;
    }
  }


  onSubmitOptionSelected(formElement: IFormElement, valueIndex: number, event: Event) {
    if (formElement.selectedIndexes) {
      formElement.selectedIndexes = [valueIndex];
    }

   // this.session.response = this.screenForm;
   // this.session.onAction(formElement.id);
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

