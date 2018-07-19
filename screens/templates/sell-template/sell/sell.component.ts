import { Component, ViewChild } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { ObservableMedia } from '@angular/flex-layout';
import { SessionService, AbstractTemplate, IMenuItem } from '../../../../core';
import { StatusBarData } from '../../../status-bar/status-bar-data';
import { SellScreenUtils, ISellScreen } from './sell-screen.interface';
import { ISellTemplate } from './sell-template.interface';
import { SellStatusSectionData } from '../sell-status-section/sell-status-section.data';

@Component({
  selector: 'app-sell',
  templateUrl: './sell.component.html',
  styleUrls: ['./sell.component.scss']
})
export class SellComponent extends AbstractTemplate {

  template: ISellTemplate;
  screen: ISellScreen;
  statusBar: StatusBarData;
  statusSection: SellStatusSectionData;

  @ViewChild('drawer') drawer;
  public drawerOpen: Observable<boolean>;

  public drawerMode: Observable<string>;

  constructor(public session: SessionService, private observableMedia: ObservableMedia) {
    super();

  }

  show(screen: any) {
    this.screen = screen;
    this.template = screen.template;
    this.statusBar = SellScreenUtils.getStatusBar(screen);
    this.statusSection = SellScreenUtils.getStatusSection(this.template);
    if (this.template.localMenuItems.length > 0) {
        this.initializeDrawerMediaSizeHandling();
      } else {
        this.drawerOpen = of(false);
      }
  }

  public doMenuItemAction(menuItem: IMenuItem) {
    this.session.onAction(menuItem.action, null, menuItem.confirmationMessage);
  }

  public isMenuItemEnabled(m: IMenuItem): boolean {
    let enabled = m.enabled;
    if (m.action.startsWith('<') && this.session.isRunningInBrowser()) {
      enabled = false;
    }
    return enabled;
  }

  private initializeDrawerMediaSizeHandling() {
    const openMap = new Map([
      ['xs', false],
      ['sm', true],
      ['md', true],
      ['lg', true],
      ['xl', true]
    ]);

    let startOpen: boolean;
    openMap.forEach((open, mqAlias) => {
      if (this.observableMedia.isActive(mqAlias)) {
        startOpen = open;
      }
    });
    this.drawerOpen = this.observableMedia.asObservable().pipe(map(
      change => {
        return openMap.get(change.mqAlias);
      }
    ),startWith(startOpen));

    const modeMap = new Map([
      ['xs', 'over'],
      ['sm', 'side'],
      ['md', 'side'],
      ['lg', 'side'],
      ['xl', 'side']
    ]);

    let startMode: string;
    modeMap.forEach((mode, mqAlias) => {
      if (this.observableMedia.isActive(mqAlias)) {
        startMode = mode;
      }
    });
    this.drawerMode = this.observableMedia.asObservable().pipe(map(
      change => {
        return modeMap.get(change.mqAlias);
      }
    ),startWith(startMode));
  }
}
