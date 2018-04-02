import { over } from '@stomp/stompjs';
import { OverlayContainer } from '@angular/cdk/overlay';
import { Component, OnInit } from '@angular/core';
import { IScreen } from '../../common/iscreen';
import { SessionService } from '../../services/session.service';
import { AbstractApp } from '../../common/abstract-app';
import { AbstractTemplate } from '../../common/abstract-template';
import { StatusBarData } from '../../common/screen-interfaces/iStatusBarData';
import { SellScreenUtils } from '../../common/screen-interfaces/iSellScreen';

@Component({
  selector: 'app-blank-with-bar',
  templateUrl: './blank-with-bar.component.html',
  styleUrls: ['./blank-with-bar.component.scss']
})
export class BlankWithBarComponent extends AbstractTemplate implements OnInit {

  template: any;
  statusBar : StatusBarData;

  constructor(public overlayContainer: OverlayContainer) {
      super();
  }

  ngOnInit() {
  }

  show(template: any, app: AbstractApp) {
    this.template = template;
    this.statusBar = SellScreenUtils.getStatusBar(template);
  }

}
