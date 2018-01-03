import { IScreen } from '../../common/iscreen';
import { Component } from '@angular/core';
import { SessionService } from '../../services/session.service';
import { AbstractApp } from '../../common/abstract-app';


@Component({
  selector: 'app-dynamic-form',
  templateUrl: './dynamic-form.component.html'
})
export class DynamicFormComponent implements IScreen {

  constructor(public session: SessionService) {
  }

  show(session: SessionService, app: AbstractApp) {
  }
}
