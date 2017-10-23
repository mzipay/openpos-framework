import { IMenuItem } from './../common/imenuitem';
import { IScreen } from '../common/iscreen';
import {Component, ViewChild, AfterViewInit, DoCheck, OnInit} from '@angular/core';
import {SessionService} from '../session.service';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html'
})
export class FormComponent implements AfterViewInit, DoCheck, IScreen, OnInit {

  public form: IForm;
  itemActions: IMenuItem[];
  private lastSequenceNum: number;

  constructor(public session: SessionService) {
    this.form = session.screen.form;
  }

  show(session: SessionService) {
  }

  ngDoCheck(): void {
    if (this.session.screen.sequenceNumber !== this.lastSequenceNum) {
      this.ngOnInit();
    }
  }

  ngOnInit(): void {
    this.itemActions = this.session.screen.itemActions;
  }

  ngAfterViewInit(): void {
  }

  onEnter(value: string) {
    this.session.onAction('Save');
  }

  onItemAction(menuItem: IMenuItem, $event): void {
    this.session.response = this.form;
    this.session.onAction(menuItem.action);
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
    placeholder: string;
    buttonAction: string;
}

