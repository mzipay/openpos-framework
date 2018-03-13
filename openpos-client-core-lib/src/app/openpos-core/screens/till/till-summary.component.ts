import { IMenuItem } from './../../common/imenuitem';
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

  nextAction: IMenuItem;

  constructor(public session: SessionService) {
  }

  show(screen: any, app: AbstractApp) {
  }

  ngOnInit() {
    this.nextAction = this.session.screen.nextAction;
  }

  onNextAction() {
    this.session.onAction(this.nextAction.action);
  }

}
