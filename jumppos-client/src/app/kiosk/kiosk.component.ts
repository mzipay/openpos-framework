import { ScreenService } from './../screen.service';
import { AbstractApp } from '../screens/abstract-app';
import { DialogComponent } from '../screens/dialog.component';
import { IMenuItem } from '../common/imenuitem';
import { Component, OnInit, OnDestroy, DoCheck } from '@angular/core';
import { SessionService } from '../session.service';
import { StatusBarComponent } from '../screens/statusbar.component';
import { FocusDirective } from '../common/focus.directive';
import { MdDialog, MdDialogRef } from '@angular/material';

@Component({
  selector: 'app-pos',
  templateUrl: './kiosk.component.html'
})
export class KioskComponent extends AbstractApp implements DoCheck {

  cartSize: number;
  showBackButton: boolean;

  constructor(screenService: ScreenService, public session: SessionService, public dialog: MdDialog) {
    super(screenService, session, dialog);
  }

  ngDoCheck(): void {
    super.ngDoCheck();
    if (this.session.screen) {
      if (this.session.screen.cart.items) {
        this.cartSize = this.session.screen.cart.items.length;
      }
      this.showBackButton = this.session.screen.type === 'Cart';
    }
  }

  ngAfterViewInit() {
    //     //document.getElementById('embeddedIFrame').contentWindow.location.href
    //     console.log('ngAfterViewInit');
    // console.log(
    //   document.getElementById('embeddedIFrame').nodeValue
    // );
    
  }

  protected appName(): String {
    return 'kiosk';
  }

}
