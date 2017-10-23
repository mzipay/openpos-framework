import { IconService } from './../icon.service';
import { ScreenService } from './../screen.service';
import { AbstractApp } from '../screens/abstract-app';
import { DialogComponent } from '../screens/dialog.component';
import { IMenuItem } from '../common/imenuitem';
import { Component, OnInit, OnDestroy, DoCheck } from '@angular/core';
import { SessionService } from '../session.service';
import { StatusBarComponent } from '../screens/statusbar.component';
import { FocusDirective } from '../common/focus.directive';
import { MatDialog, MatDialogRef, MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-pos',
  templateUrl: './kiosk.component.html'
})
export class KioskComponent extends AbstractApp implements DoCheck {

  cartSize: number;

  constructor(public screenService: ScreenService,
    public session: SessionService,
    public dialog: MatDialog,
    public iconService: IconService,
    public snackBar: MatSnackBar) {
    super(screenService, session, dialog, iconService, snackBar);
  }

  ngDoCheck(): void {
    super.ngDoCheck();
    if (this.session.screen) {
      if (this.session.screen.cart && this.session.screen.cart.items) {
        this.cartSize = this.session.screen.cart.items.length;
      }
    }
  }

  protected appName(): String {
    return 'kiosk';
  }

}
