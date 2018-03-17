import { IScreen } from './../../common/iscreen';
import { Router } from '@angular/router';
import { Component, ViewChild, AfterViewInit, DoCheck } from '@angular/core';
import { SessionService } from '../../services/session.service';
import { AbstractApp } from '../../common/abstract-app';

@Component({
  selector: 'app-full-page-image',
  templateUrl: './full-page-image.component.html'
})
export class FullPageImageComponent implements IScreen {

  constructor(private session: SessionService,
    private router: Router) {
  }

  show(screen: any, app: AbstractApp) {
  }

}
