import { Component } from '@angular/core';
import { IScreen } from '../../common/iscreen';
import { SessionService } from '../../services/session.service';


@Component({
  selector: 'app-print-preview',
  templateUrl: './print-preview.component.html'
})
export class PrintPreviewComponent implements IScreen {

  screen: any;

  constructor(public session: SessionService) {
  }

  show(screen: any) {
    this.screen = screen;
  }


}
