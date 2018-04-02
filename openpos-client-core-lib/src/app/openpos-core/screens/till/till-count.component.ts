import { IForm } from './../form.component';
import { IMenuItem } from './../../common/imenuitem';
import { SessionService } from './../../services/session.service';
import { IScreen } from './../../common/iscreen';
import { Component, OnInit, ViewChild } from '@angular/core';
import { AbstractApp } from '../../common/abstract-app';
import { IFormElement } from '../../common/iformfield';

@Component({
  selector: 'app-till-count',
  templateUrl: './till-count.component.html',
  styleUrls: ['./till-count.component.scss']
})
export class TillCountComponent implements OnInit, IScreen {
  screen: any;
  nextAction: IMenuItem;
  public form: IForm;
  @ViewChild('tillForm') tillForm;

  constructor(public session: SessionService) {
  }

  show(screen: any, app: AbstractApp) {
    this.screen = screen;
  }

  ngOnInit() {
    this.form = this.screen.form;
    this.nextAction = this.screen.nextAction;
  }

  onFieldChanged(eventData: {formElement: IFormElement, event: Event}) {
    this.form = this.screen.form;
    this.session.response = this.form;
    this.session.onAction('formChanged');
  }

  onNextAction() {
    this.session.response = this.screen.form;
    this.session.onAction(this.nextAction.action);
  }

}
