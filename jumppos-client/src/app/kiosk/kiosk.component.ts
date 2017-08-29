import { ScreenService } from './../screen.service';
import { AbstractApp } from '../screens/abstract-app';
import { DialogComponent } from '../screens/dialog.component';
import { IMenuItem } from '../screens/imenuitem';
import { Component, OnInit, OnDestroy, DoCheck } from '@angular/core';
import { SessionService } from '../session.service';
import { StatusBarComponent } from '../screens/statusbar.component';
import { FocusDirective } from '../screens/focus';
import { MdDialog, MdDialogRef } from '@angular/material';

@Component({
  selector: 'app-pos',
  templateUrl: './kiosk.component.html'
})
export class KioskComponent extends AbstractApp {

  constructor(screenService: ScreenService, public session: SessionService, public dialog: MdDialog) {
    super(screenService, session, dialog);
  }

  protected appName(): String {
    return 'kiosk';
  }

}
