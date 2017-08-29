import { IScreen } from '../common/iscreen';
import { IMenuItem } from '../common/imenuitem';
// import {MdButtonModule, MdCheckboxModule} from '@angular/material';
import { Component, ViewChild, AfterViewInit, DoCheck } from '@angular/core';
import { SessionService } from '../session.service';


@Component({
  selector: 'app-embedded-web-page',
  templateUrl: './embedded-web-page.component.html'
})
export class EmbeddedWebPageComponent implements IScreen {

  public url: string;

  constructor(public session: SessionService) {
  }

  show(session: SessionService) {
    this.url = session.screen.url;
  }

}
