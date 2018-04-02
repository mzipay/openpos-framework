import { Component, OnInit } from '@angular/core';

import { SessionService } from '../../services/session.service';
import { AbstractApp } from '../../common/abstract-app';
import { IScreen } from '../..';

@Component({
  selector: 'app-call-for-authorization-component',
  templateUrl: './call-for-authorization.component.html'
})
export class CallForAuthorizationComponent implements OnInit, IScreen {

  prompt: string = "";
  instructions: string = "";

  constructor() { }

  show(screen: any, app: AbstractApp) {

    this.prompt = screen.prompt;
    this.instructions = screen.instructions;
  }

  ngOnInit() {
  }
}