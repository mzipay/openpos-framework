import { SessionService } from './../../services/session.service';
import { IScreen } from './../../common/iscreen';
import { Component, OnInit } from '@angular/core';
import { AbstractApp } from '../../common/abstract-app';

@Component({
  selector: 'app-till-summary',
  templateUrl: './till-summary.component.html',
  styleUrls: ['./till-summary.component.scss']
})
export class TillSummaryComponent implements OnInit, IScreen {

  session: SessionService;

  show(session: SessionService, app: AbstractApp) {
    this.session = session;
  }

  constructor() { }

  ngOnInit() {
  }

}
