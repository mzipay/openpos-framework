import { IScreen } from '../common/iscreen';
import {Component, ViewChild, AfterViewInit, DoCheck} from '@angular/core';
import {SessionService} from '../session.service';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html'
})
export class FormComponent implements AfterViewInit, DoCheck, IScreen {

  public form: IForm;

  constructor(public session: SessionService) {
    this.form = session.screen.form;
  }

  show(session: SessionService) {
  }

  ngDoCheck(): void {
  }

  ngAfterViewInit(): void {
  }

  onEnter(value: string) {
    this.session.onAction('Save');
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
    fieldId: string;
    value: string;
    placeholder: string;
    buttonAction: string;
}

