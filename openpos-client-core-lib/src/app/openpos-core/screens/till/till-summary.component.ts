import { IMenuItem } from './../../common/imenuitem';
import { SessionService } from './../../services/session.service';
import { IScreen } from './../../common/iscreen';
import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { AbstractApp } from '../../common/abstract-app';
import { FormComponent } from '../form.component';

@Component({
  selector: 'app-till-summary',
  templateUrl: './till-summary.component.html',
  styleUrls: ['./till-summary.component.scss']
})
export class TillSummaryComponent implements OnInit, AfterViewInit, IScreen {

  nextAction: IMenuItem;
  screen: any;
  @ViewChild(FormComponent) formComponent;

  constructor(public session: SessionService) {
  }

  show(screen: any, app: AbstractApp) {
    this.screen = screen;
    this.formComponent.show(screen, app);
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
