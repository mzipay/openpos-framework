import { Router } from '@angular/router';
import { Component } from '@angular/core';
import { IScreen } from '../../common/iscreen';
import { SessionService } from '../../services/session.service';

@Component({
  selector: 'app-embedded-web-page',
  templateUrl: './embedded-web-page.component.html'
})
export class EmbeddedWebPageComponent implements IScreen {

  screen: any;

  constructor(private session: SessionService,
    private router: Router) {
  }

  show(screen: any) {
    this.screen = screen;
  }

}
