import { Component } from '@angular/core';
import { SessionService } from '../services/session.service';
import { AbstractApp } from '../common/abstract-app';
import { IScreen } from '..';


@Component({
  selector: 'app-print-preview',
  templateUrl: './print-preview.component.html'
})
export class PrintPreviewComponent implements IScreen {

  constructor(public session: SessionService) {
  }

  show(screen: any, app: AbstractApp) {
  }


}
