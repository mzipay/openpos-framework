import { DeviceService } from '../../services/device.service';
import { ISellItem } from '../../common/isellitem';
import { IScreen } from '../../common/iscreen';
import { IMenuItem } from '../../common/imenuitem';
import { Component, ViewChild, AfterViewInit, AfterContentInit, DoCheck, OnInit} from '@angular/core';
import { SessionService } from '../../services/session.service';
import { AbstractApp } from '../../common/abstract-app';
import { ObservableMedia } from '@angular/flex-layout';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs/Observable';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.scss']
})
export class TransactionComponent implements AfterViewInit, DoCheck, IScreen, OnInit {


  @ViewChild('box') vc;
  initialized = false;

  public overFlowListSize: Observable<number>;

  public items: ISellItem[];

  constructor(public session: SessionService, devices: DeviceService, private observableMedia: ObservableMedia) {

    }

  show(screen: any, app: AbstractApp) {
  }

  ngDoCheck(): void {
    if (typeof this.session.screen !== 'undefined') {
      this.items = this.session.screen.items;
    }
  }

  ngOnInit(): void {
    const sizeMap = new Map([
      ['xs', 3],
      ['sm', 3],
      ['md', 4],
      ['lg', 5],
      ['xl', 5]
    ]);

    let startSize = 3;
    sizeMap.forEach((size, mqAlias) => {
      if( this.observableMedia.isActive(mqAlias)){
        startSize = size;
      }
    });
    this.overFlowListSize = this.observableMedia.asObservable().map(
      change => {
        return sizeMap.get(change.mqAlias);
      }
    ).startWith(startSize);
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
