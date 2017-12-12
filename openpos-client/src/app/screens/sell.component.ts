import { DeviceService } from './../services/device.service';
import { ISellItem } from '../common/isellitem';
import { IScreen } from '../common/iscreen';
import { IMenuItem } from '../common/imenuitem';
import {Component, ViewChild, AfterViewInit, DoCheck, OnInit} from '@angular/core';
import {SessionService} from '../services/session.service';
import { AbstractApp } from './abstract-app';
import { ScanSomethingComponent } from '../common/controls/scan-something/scan-something.component';

@Component({
  selector: 'app-sell',
  templateUrl: './sell.component.html',
  styleUrls: ['./sell.component.scss']
})
export class SellComponent implements AfterViewInit, DoCheck, IScreen, OnInit {

  @ViewChild('box') vc;

  initialized = false;
  scanInputCallback: Function;

  public items: ISellItem[];

  constructor(public session: SessionService, devices: DeviceService) {

  }

  public ngOnInit(): void {
    this.scanInputCallback = this.onScanInputEnter.bind(this);
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

  onScanInputEnter($event, scanInput: ScanSomethingComponent): void {
    if (scanInput.responseText) {
        this.session.response = scanInput.responseText;
        this.session.screen.responseText = null;
        scanInput.responseText = null;
        this.session.onAction('Next');
        if ($event.target && $event.target.disabled) {
            $event.target.disabled = true;
        }
    }
  }


  onEnter(value: string) {
    this.session.response = value;
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
