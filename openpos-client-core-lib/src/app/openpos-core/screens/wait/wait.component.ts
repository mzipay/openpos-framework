import { Component, OnInit } from '@angular/core';

import { SessionService } from '../../services/session.service';
import { AbstractApp } from '../../common/abstract-app';

@Component({
  selector: 'app-wait-component',
  templateUrl: './wait.component.html'
})
export class WaitComponent implements OnInit {

  instructions: string = "";
  icon: string = "";

  constructor(public session: SessionService) { }

  show(screen: any, app: AbstractApp) {
  }

  ngOnInit() {
    this.instructions = this.session.screen.instructions;
    this.icon = this.session.screen.icon;
  }
}