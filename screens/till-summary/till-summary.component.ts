
import { Component, OnInit, AfterViewInit } from '@angular/core';

import { IMenuItem } from './../../common/imenuitem';
import { SessionService } from './../../services/session.service';
import { IScreen } from './../../common/iscreen';

@Component({
  selector: 'app-till-summary',
  templateUrl: './till-summary.component.html',
  styleUrls: ['./till-summary.component.scss']
})
export class TillSummaryComponent implements OnInit, AfterViewInit, IScreen {

  nextAction: IMenuItem;
  screen: any;

  constructor(public session: SessionService) {
  }

  show(screen: any) {
    this.screen = screen;
  }

  ngOnInit() {
    this.nextAction = this.screen.nextAction;
  }

  ngAfterViewInit(): void {
  }

  onNextAction() {
    this.session.onAction(this.nextAction.action);
  }

}
