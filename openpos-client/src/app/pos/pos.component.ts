import { ScreenService } from './../services/screen.service';
import { AbstractApp } from '../screens/abstract-app';
import { IMenuItem } from '../common/imenuitem';
import { Component, DoCheck } from '@angular/core';
import { SessionService } from '../services/session.service';
import { FocusDirective } from '../common/focus.directive';
import { MatDialog, MatDialogRef, MatSnackBar } from '@angular/material';
import { IconService } from './../services/icon.service';
import {OverlayContainer} from '@angular/cdk/overlay';

@Component({
  selector: 'app-pos',
  templateUrl: './pos.component.html'
})
export class PosComponent extends AbstractApp implements DoCheck {

  // TODO these should be conflated or removed perhaps.
  public menuItems: IMenuItem[] = [];
  public menuActions: IMenuItem[] = [];
  public backButton: IMenuItem;
  public isCollapsed = true;

  constructor(public screenService: ScreenService, public session: SessionService, public dialog: MatDialog,
    public iconService: IconService, public snackBar: MatSnackBar, public overlayContainer: OverlayContainer) {
    super(screenService, session, dialog, iconService, snackBar, overlayContainer);
  }

  public appName(): string {
    return 'pos';
  }

  ngDoCheck(): void {
    if (typeof this.session.screen !== 'undefined') {
      this.menuItems = this.session.screen.menuItems;
      this.menuActions = this.session.screen.menuActions;
      this.backButton = this.session.screen.backButton;
      if (!this.menuActions || this.menuActions.length === 0) {
        this.isCollapsed = true;
      }
    }

    super.ngDoCheck();
  }

}
