import { IMenuItem } from './../../common/imenuitem';
import { IScreen } from '../../common/iscreen';
import { Component, ViewChild, AfterViewInit, DoCheck, OnInit, Output, Input } from '@angular/core';
import {SessionService} from '../../services/session.service';
import { MatSelectChange } from '@angular/material';
import { AbstractApp } from '../../common/abstract-app';
import { FormArray, FormBuilder, FormGroup, Validators, AbstractControl, FormControl } from '@angular/forms';
import { IFormElement } from '../../common/iformfield';


@Component({
  selector: 'app-dynamic-form',
  templateUrl: './dynamic-form.component.html'
})
export class DynamicFormComponent implements AfterViewInit, DoCheck, IScreen, OnInit {

  public screenForm: IForm;
  private lastSequenceNum: number;

  @Input() formFields: IFormElement[];
  formGroup: FormGroup;
  payload = '';

  constructor(public session: SessionService, public formBuilder: FormBuilder) {
  }

  show(session: SessionService, app: AbstractApp) {
  }

  ngDoCheck(): void {
    this.ngOnInit();
    if (this.session.screen.sequenceNumber !== this.lastSequenceNum) {
      this.ngOnInit();
      this.lastSequenceNum = this.session.screen.sequenceNumber;
    }
  }

  ngOnInit(): void {
    this.screenForm = this.session.screen.form;

    const newGroup: any = {};
    this.screenForm.formElements.forEach(element => {
      if (element.required) {
        newGroup[element.id] = new FormControl(element.value || '', Validators.required);
      } else {
        newGroup[element.id] = new FormControl(element.value || '');
      }
      // newGroup[element.id] = element.required ? new FormControl(element.value || '', Validators.required)
      //                                         : new FormControl(element.value || '');
    });

    this.formGroup = new FormGroup(newGroup);

    // Bind to the screen
    this.formFields = this.session.screen.form.formFields;
  }

  ngAfterViewInit(): void {
  }

  createItem(): FormGroup {
    return this.formBuilder.group({
      name: '',
      description: '',
      price: ''
    });
  }
  
  onEnter(value: string) {
    // If there is a button which is a submitButton, submit the form
    // with that button's action
    const submitButtons: IFormElement[] = this.screenForm.formElements.filter(
      elem => elem.elementType === 'Button' && elem.submitButton);

    if (submitButtons.length > 0) {
      this.session.response = this.screenForm;
      this.session.onAction(submitButtons[0].buttonAction);
    }
  }

  onItemAction(menuItem: IMenuItem, $event): void {
    this.session.response = this.screenForm;
    this.session.onAction(menuItem.action);
  }

  onButtonAction(action: string) {
    this.session.response = this.screenForm;
    this.session.onAction(action);
  }

  onComboBoxSelectionChange(formElement: IFormElement, event: Event): void {
    if (formElement.selectedIndexes) {
      console.log(event);
      formElement.selectedIndexes = [event['value']];
    }
  }

  onSubmitOptionSelected(formElement: IFormElement, valueIndex: number, event: Event) {
    if (formElement.selectedIndexes) {
      formElement.selectedIndexes = [valueIndex];
    }

    this.session.response = this.screenForm;
    this.session.onAction(formElement.id);
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
    name: string;
    formElements: IFormElement[];
}
