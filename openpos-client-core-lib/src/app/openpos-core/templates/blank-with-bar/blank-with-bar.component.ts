import { over } from '@stomp/stompjs';
import { OverlayContainer } from '@angular/cdk/overlay';
import { Component, OnInit } from '@angular/core';
import { IScreen } from '../../common/iscreen';
import { SessionService } from '../../services/session.service';
import { AbstractApp } from '../../common/abstract-app';
import { AbstractTemplate } from '../../common/abstract-template';

@Component({
  selector: 'app-blank-with-bar',
  templateUrl: './blank-with-bar.component.html',
  styleUrls: ['./blank-with-bar.component.scss']
})
export class BlankWithBarComponent extends AbstractTemplate implements OnInit {

  screen: any;

  constructor(public overlayContainer: OverlayContainer) {
      super();
  }

  ngOnInit() {
  }

  show(screen: any, app: AbstractApp) {
    this.screen = screen;
  }

}
