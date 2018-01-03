import { SessionService } from './../../services/session.service';
import { IScreen } from './../../common/iscreen';
import { Component, OnInit } from '@angular/core';
import { AbstractApp } from '../../common/abstract-app';

@Component({
  selector: 'app-options',
  templateUrl: './options.component.html',
  styleUrls: ['./options.component.scss']
})
export class OptionsComponent implements OnInit, IScreen {

  constructor(public session: SessionService) { }

  ngOnInit() {
  }

  show(session: SessionService, app: AbstractApp) {

  }

}
