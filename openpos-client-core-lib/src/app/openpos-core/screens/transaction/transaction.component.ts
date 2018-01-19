import { DeviceService } from '../../services/device.service';
import { ISellItem } from '../../common/isellitem';
import { IScreen } from '../../common/iscreen';
import { IMenuItem } from '../../common/imenuitem';
import { Component, ViewChild, AfterViewInit, DoCheck} from '@angular/core';
import { SessionService } from '../../services/session.service';
import { AbstractApp } from '../../common/abstract-app';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.scss']
})
export class TransactionComponent implements AfterViewInit, DoCheck, IScreen {

  @ViewChild('box') vc;
  initialized = false;

  public items: ISellItem[];

  constructor(public session: SessionService, devices: DeviceService) {

    }

  show(session: SessionService, app: AbstractApp) {
  }

  ngDoCheck(): void {
    if (typeof this.session.screen !== 'undefined') {
      this.items = this.session.screen.items;
    }
  }

  ngAfterViewInit(): void {
    this.initialized = true;
  }

  public doMenuItemAction(menuItem: IMenuItem, payLoad: any) {
      this.session.onAction(menuItem.action, payLoad, menuItem.confirmationMessage);
}

  onEnter(value: string) {
    this.session.response = value;
    this.session.onAction('Next');
  }

  public isMenuItemEnabled(m: IMenuItem): boolean {
    let enabled = m.enabled;
    if (m.action.startsWith('<') && this.session.isRunningInBrowser()) {
         enabled = false;
    }
    return enabled;
  }

}
