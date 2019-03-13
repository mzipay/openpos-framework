import { OverlayContainer } from '@angular/cdk/overlay';
import { Component, OnInit } from '@angular/core';
import { AbstractTemplate } from '../../../core';
import { StatusBarData } from '../../../shared';
import { SellScreenUtils } from '../sell-template/sell/sell-screen.interface';
import { ScreenComponent } from '../../../shared/decorators/screen.decorator';

@ScreenComponent({
    name: 'BlankWithBar',
    moduleName: 'Core'
})
@Component({
  selector: 'app-blank-with-bar',
  templateUrl: './blank-with-bar.component.html',
  styleUrls: ['./blank-with-bar.component.scss']
})
export class BlankWithBarComponent extends AbstractTemplate<any> implements OnInit {

  template: any;
  statusBar: StatusBarData;

  constructor(public overlayContainer: OverlayContainer) {
      super();
  }

  ngOnInit() {
  }

  show(template: any) {
    this.template = template;
    this.statusBar = SellScreenUtils.getStatusBar(template);
  }

  buildTemplate() {}

}
