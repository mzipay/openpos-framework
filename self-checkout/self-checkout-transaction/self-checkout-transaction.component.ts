import { Component, ViewChild, AfterViewInit, AfterViewChecked, ElementRef, OnInit } from '@angular/core';
import { ObservableMedia } from '@angular/flex-layout';
import { DeviceService, SessionService, IScreen, ISellItem, IMenuItem } from '../../core';

@Component({
  selector: 'app-self-checkout-transaction',
  templateUrl: './self-checkout-transaction.component.html',
  styleUrls: ['./self-checkout-transaction.component.scss']
})
export class SelfCheckoutTransactionComponent implements AfterViewInit, AfterViewChecked, IScreen, OnInit {

  screen: any;
  @ViewChild('scrollList') private scrollList: ElementRef;

  initialized = false;

  public items: ISellItem[];
  public size = -1;

  constructor(public session: SessionService, devices: DeviceService, private observableMedia: ObservableMedia) {
  }

  show(screen: any) {
    this.screen = screen;

    this.items = this.screen.items;
  }

  ngOnInit(): void {
    this.scrollToBottom();
  }

  ngAfterViewChecked() {
    if (this.items && this.size !== this.items.length) {
      this.scrollToBottom();
      this.size = this.items.length;
    }
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
