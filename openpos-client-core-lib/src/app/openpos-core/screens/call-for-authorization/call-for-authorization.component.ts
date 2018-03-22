import { Component, OnInit } from '@angular/core';

import { SessionService } from '../../services/session.service';
import { AbstractApp } from '../../common/abstract-app';

@Component({
  selector: 'app-call-for-authorization-component',
  templateUrl: './call-for-authorization.component.html'
})
export class CallForAuthorizationComponent implements OnInit {

  prompt: string = "";
  instructions: string = "";

  constructor(public session: SessionService) { }

  show(screen: any, app: AbstractApp) {
  }

  ngOnInit() {
    this.prompt = this.session.screen.prompt;
    this.instructions = this.session.screen.instructions;
  }
}