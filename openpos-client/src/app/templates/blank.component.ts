import { Component, OnInit } from '@angular/core';
import { IScreen } from '../common/iscreen';
import { SessionService } from '../services/session.service';
import { AbstractApp } from '../common/abstract-app';

@Component({
  selector: 'app-blank',
  templateUrl: './blank.component.html',
  styleUrls: ['./blank.component.scss']
})
export class BlankComponent implements OnInit, IScreen {

  constructor() { }

  ngOnInit() {
  }

  show(session: SessionService, app: AbstractApp) {

  }
}
