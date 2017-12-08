import { DeviceService } from './../services/device.service';
import { ISellItem } from '../common/isellitem';
import { IScreen } from '../common/iscreen';
import { IMenuItem } from '../common/imenuitem';
import {Component, ViewChild, AfterViewInit, DoCheck} from '@angular/core';
import {SessionService} from '../services/session.service';
import { AbstractApp } from './abstract-app';

@Component({
  selector: 'app-sell',
  templateUrl: './sell.component.html'
})
export class SellComponent implements AfterViewInit, DoCheck, IScreen {

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

  private filterBarcodeValue(val: string): string {
    if (!val) {
      return val;
    }
    // Filter out extra characters permitted by HTML5 input type=number (for exponentials)
    const pattern = /[e|E|\+|\-|\.]/g;

    return val.toString().replace(pattern, '');
  }

  onBarcodeKeydown(event: KeyboardEvent) {
    /*
    console.log(`[onBarcodeKeydown] key: ${event.key}`);
    console.log(`[onBarcodeKeydown] fromCharCode: ${String.fromCharCode(event.keyCode)}`);
    console.log(`[onBarcodeKeydown] keyCode: ${event.keyCode}`);
    */
    if (event.altKey || event.ctrlKey || event.metaKey ) {
      return true;
    }
    const filteredKey = this.filterBarcodeValue(event.key);
    console.log(`[onBarcodeKeydown] filtered key: ${filteredKey}`);
    return filteredKey !== null && filteredKey.length !== 0;
  }

  onBarcodePaste(event: ClipboardEvent) {
    const content = event.clipboardData.getData('text/plain');
    // console.log(`[onBarcodePaste]: ${content}`);
    const filteredContent = this.filterBarcodeValue(content);
    if (filteredContent !== content) {
      console.log(`Clipboard data contains invalid characters for barcode, suppressing pasted content '${content}'`);
    }
    return filteredContent === content;
  }

  onEnter(value: string) {
    this.session.onAction('Next');
  }

  onItemClick($event): void {
    this.session.onActionWithStringPayload(this.session.screen.itemActionName, $event.item.index);
  }

  public doMenuItemAction(menuItem: IMenuItem) {
      this.session.onAction(menuItem.action);
  }

  public isMenuItemEnabled(m: IMenuItem): boolean {
    let enabled = m.enabled;
    if (m.action.startsWith('<') && this.session.isRunningInBrowser()) {
         enabled = false;
    }
    return enabled;
  }

}
