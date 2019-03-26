import { Component, ViewChild, AfterViewInit, AfterViewChecked, ElementRef, OnInit } from '@angular/core';
import { ObservableMedia } from '@angular/flex-layout';
import { IScreen } from '../../core/components/dynamic-screen/screen.interface';
import { ISellItem } from '../../core/interfaces/sell-item.interface';
import { SessionService } from '../../core/services/session.service';
import { DeviceService } from '../../core/services/device.service';
import { IActionItem } from '../../core/interfaces/menu-item.interface';

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

  public doMenuItemAction(menuItem: IActionItem) {
    this.session.onAction(menuItem);
  }

  scrollToBottom(): void {
    try {
      this.scrollList.nativeElement.scrollTop = this.scrollList.nativeElement.scrollHeight;
    } catch (err) { }
  }

}
