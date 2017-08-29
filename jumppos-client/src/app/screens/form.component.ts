import { IScreen } from './iscreen';
// import {MdButtonModule, MdCheckboxModule} from '@angular/material';
import {Component, ViewChild, AfterViewInit, DoCheck} from '@angular/core';
import {SessionService} from '../session.service';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html'
})
export class FormComponent implements AfterViewInit, DoCheck, IScreen {

  initialized = false;

  public form: IForm;

  constructor(public session: SessionService) {
    this.form = session.screen.form;
  }

  show(session: SessionService) {
  }

  ngDoCheck(): void {
    if (this.initialized) {
  //    this.vc.nativeElement.focus();
    }
  }

  ngAfterViewInit(): void {
    console.log('ngAfterViewInit');
    this.initialized = true;
  }

  onEnter(value: string) {
    this.session.onAction('Save');
  }

}

export interface IForm {
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

