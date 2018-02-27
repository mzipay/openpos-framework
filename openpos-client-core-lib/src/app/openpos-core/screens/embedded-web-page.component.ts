import { Router } from '@angular/router';
import { IScreen } from '../common/iscreen';
import { IMenuItem } from '../common/imenuitem';
import { Component, ViewChild, AfterViewInit, DoCheck } from '@angular/core';
import { SessionService } from '../services/session.service';
import { AbstractApp } from '../common/abstract-app';

@Component({
  selector: 'app-embedded-web-page',
  templateUrl: './embedded-web-page.component.html'
})
export class EmbeddedWebPageComponent implements IScreen {

  constructor(private session: SessionService,
    private router: Router) {
  }

  show(session: SessionService, app: AbstractApp) {
  }

}
