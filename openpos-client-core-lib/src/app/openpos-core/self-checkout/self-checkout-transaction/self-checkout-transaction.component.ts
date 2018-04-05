import { DeviceService } from '../../services/device.service';
import { ISellItem } from '../../common/isellitem';
import { IScreen } from '../../common/iscreen';
import { IMenuItem } from '../../common/imenuitem';
import { Component, ViewChild, AfterViewInit, AfterContentInit, AfterViewChecked, ElementRef, DoCheck, OnInit } from '@angular/core';
import { SessionService } from '../../services/session.service';
import { AbstractApp } from '../../common/abstract-app';
import { ObservableMedia } from '@angular/flex-layout';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';

@Component({
  selector: 'app-self-checkout-transaction',
  templateUrl: './self-checkout-transaction.component.html',
  styleUrls: ['./self-checkout-transaction.component.scss']
})
export class SelfCheckoutTransactionComponent implements AfterViewInit, IScreen, OnInit {

  screen: any;
  @ViewChild('scrollList') private scrollList: ElementRef;

  initialized = false;

  public items: ISellItem[];
  public size = -1;

  constructor(public session: SessionService, devices: DeviceService, private observableMedia: ObservableMedia) {
  }

  show(screen: any, app: AbstractApp) {
    this.screen = screen;

    this.items = this.screen.items;
  }

  ngOnInit(): void {
    this.scrollToBottom();
  }

  ngAfterViewInit(): void {
    this.initialized = true;
  }

  public doMenuItemAction(menuItem: IMenuItem) {
    this.session.onAction(menuItem.action, null, menuItem.confirmationMessage);
  }

  scrollToBottom(): void {
    try {
      this.scrollList.nativeElement.scrollTop = this.scrollList.nativeElement.scrollHeight;
    } catch (err) { }
  }

}
