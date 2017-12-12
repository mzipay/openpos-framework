import { IMenuItem } from './../common/imenuitem';
import { IScreen } from '../common/iscreen';
import { Component, ViewChild, AfterViewInit, DoCheck, OnInit, Output } from '@angular/core';
import {SessionService} from '../services/session.service';
import { MatSelectChange } from '@angular/material';
import { AbstractApp } from './abstract-app';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html'
})
export class FormComponent implements AfterViewInit, DoCheck, IScreen, OnInit {

  public form: IForm;
  private lastSequenceNum: number;
  formButtons: IFormElement[];

  constructor(public session: SessionService) {
  }

  show(session: SessionService, app: AbstractApp) {
  }

  ngDoCheck(): void {
    if (this.session.screen.sequenceNumber !== this.lastSequenceNum) {
      this.ngOnInit();
      this.lastSequenceNum = this.session.screen.sequenceNumber;
    }
  }

  ngOnInit(): void {
    this.form = this.session.screen.form;
    this.formButtons = this.session.screen.form.formElements.filter((e) => e.elementType === 'Button');
  }

  ngAfterViewInit(): void {
  }

  onEnter(value: string) {
    // If there is a button which is a submitButton, submit the form
    // with that button's action
    const submitButtons: IFormElement[] = this.form.formElements.filter(
      elem => elem.elementType === 'Button' && elem.submitButton);

    if (submitButtons.length > 0) {
      this.session.response = this.form;
      this.session.onAction(submitButtons[0].buttonAction);
    }
  }

  onItemAction(menuItem: IMenuItem, $event): void {
    this.session.response = this.form;
    this.session.onAction(menuItem.action);
  }

  onButtonAction(action: string) {
    this.session.response = this.form;
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

    this.session.response = this.form;
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

export interface IFormElement {
    elementType: string;
    inputType: string;
    label: string;
    id: string;
    value: string;
    values: string[];
    placeholder: string;
    buttonAction: string;
    submitButton: boolean;
    required: boolean;
    selectedIndexes:  number[];
}

