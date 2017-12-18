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

  nextAction: IMenuItem;
  public form: IForm;
  @ViewChild('tillForm') tillForm;

  constructor(public session: SessionService) {
  }

  show(session: SessionService, app: AbstractApp) {
  }

  ngOnInit() {
    this.form = this.session.screen.form;
    this.nextAction = this.session.screen.menuItems.find(m => m.action === 'Next');
  }

  onFieldChanged(eventData: {formElement: IFormElement, event: Event}) {
    this.form = this.session.screen.form;
    this.session.response = this.form;
    this.session.onAction('formChanged');
  }

  onNextAction() {
    this.session.response = this.session.screen.form;
    this.session.onAction(this.nextAction.action);
  }

}
