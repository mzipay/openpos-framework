import { IMenuItem } from '../../common/imenuitem';
import { IconComponent } from '../../common/controls/icon.component';
import { IScreen } from '../../common/iscreen';
import { Component, ViewChild, OnInit } from '@angular/core';
import { SessionService } from '../../services/session.service';
import {MediaChange, ObservableMedia} from '@angular/flex-layout';
import { AbstractApp } from '../../common/abstract-app';
import { IUrlMenuItem } from '../../common/iurlmenuitem';

@Component({
  selector: 'app-self-checkout-home',
  templateUrl: './self-checkout-home.component.html',
  styleUrls: ['./self-checkout-home.component.scss']

})
export class SelfCheckoutHomeComponent implements IScreen, OnInit {

  public menuItems: IMenuItem[];
  gutterSize = 40;
  gridColumns = 3;

  constructor(public session: SessionService, public media: ObservableMedia) {
    this.menuItems = session.screen.menuItems;
  }

  ngOnInit() {
    this.updateGrid();
    this.media.subscribe(() => {
        this.updateGrid();
    });
  }

  private updateGrid(): void {
    const isLarge = (this.media.isActive('xl') || this.media.isActive('lg') || this.media.isActive('md'));
    const isSmall = (this.media.isActive('sm'));
    this.gridColumns = isLarge ? 3 : (isSmall ? 2 : 1);
    this.gutterSize = isLarge ? 20 : 10;
  }

  show(screen: any, app: AbstractApp) {
  }

  onEnter(value: string) {
    this.session.onAction('Save');
  }

  getClass(): String {
    // return 'main-menu-grid-list';
    return 'foo';
  }

  onMenuItemClick(menuItem: IMenuItem) {
      this.session.onAction(menuItem, null, menuItem.confirmationMessage );
  }
}
