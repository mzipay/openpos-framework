import { IMenuItem } from './../../common/imenuitem';
import { Component, ViewChild } from '@angular/core';
import { IFormElement } from '../../common/iformfield';
import { IForm } from '../../common/iform';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
  selector: 'app-till-count',
  templateUrl: './till-count.component.html',
  styleUrls: ['./till-count.component.scss']
})
export class TillCountComponent extends PosScreen<any> {

  nextAction: IMenuItem;
  public form: IForm;
  @ViewChild('tillForm') tillForm;

  constructor() {
      super();
  }

  buildScreen() {
    // After screen is initialized, all we need to do is
    // get an updated total from the server.  This saves
    // unnecessary rebuilding of the screen
    if (!this.screen) {
      this.form = this.screen.form;
      this.nextAction = this.screen.nextAction;
    } else {
      this.screen.total = this.screen.total;
    }
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
