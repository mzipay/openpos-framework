import { over } from '@stomp/stompjs';
import { OverlayContainer } from '@angular/cdk/overlay';
import { Component, OnInit } from '@angular/core';
import { IScreen } from '../../common/iscreen';
import { SessionService } from '../../services/session.service';
import { AbstractApp } from '../../common/abstract-app';
import { AbstractTemplate } from '../../common/abstract-template';

@Component({
  selector: 'app-blank',
  templateUrl: './blank.component.html',
  styleUrls: ['./blank.component.scss']
})
export class BlankComponent extends AbstractTemplate implements OnInit {

  constructor(public overlayContainer: OverlayContainer) {
      super();
  }

  ngOnInit() {
  }

}
