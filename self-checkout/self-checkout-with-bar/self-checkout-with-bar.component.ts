
import { OverlayContainer } from '@angular/cdk/overlay';
import { Component, OnInit } from '@angular/core';
import { ISellTemplate } from '../../screens/templates/sell-template/sell/isell-template';
import { AbstractTemplate } from '../../common/abstract-template';
import { SellScreenUtils } from '../../screens/templates/sell-template/sell/iSellScreen';
import { SelfCheckoutStatusBarData } from '../self-checkout-statusbar/self-checkout-status-bar-data';

@Component({
  selector: 'app-selfcheckout-with-bar',
  templateUrl: './self-checkout-with-bar.component.html',
  styleUrls: ['./self-checkout-with-bar.component.scss']
})
export class SelfCheckoutWithBarComponent extends AbstractTemplate implements OnInit {

  template: ISellTemplate;
  statusBar: SelfCheckoutStatusBarData;

  constructor(public overlayContainer: OverlayContainer) {
      super();
  }

  ngOnInit() {
  }

  show(screen: any) {
    this.template = screen.template;
    this.statusBar = SellScreenUtils.getSelfCheckoutStatusBar( screen );
  }

}

