import { DeviceService } from './../services/device.service';
import { ScreenService } from './../services/screen.service';
import { AbstractApp } from '../common/abstract-app';
import { IMenuItem } from '../common/imenuitem';
import { Component, DoCheck, ViewChild } from '@angular/core';
import { SessionService } from '../services/session.service';
import { FocusDirective } from '../common/focus.directive';
import { MatDialog, MatDialogRef, MatSnackBar } from '@angular/material';
import { IconService } from './../services/icon.service';
import { OverlayContainer } from '@angular/cdk/overlay';
import { Router } from '@angular/router';


@Component({
  selector: 'app-selfcheckout',
  templateUrl: './selfcheckout.component.html',
  styleUrls: ['./selfcheckout.component.scss']
})
export class SelfCheckoutComponent extends AbstractApp implements DoCheck {

  public backButton: IMenuItem;

  constructor(public screenService: ScreenService, public session: SessionService,
    public deviceService: DeviceService, public dialog: MatDialog,
    public iconService: IconService, public snackBar: MatSnackBar, public overlayContainer: OverlayContainer,
    protected router: Router) {
    super(screenService, session, dialog, iconService, snackBar, overlayContainer, router);
  }

  public appName(): string {
    return 'selfcheckout';
  }

  ngDoCheck(): void {
    if (typeof this.session.screen !== 'undefined') {
      this.backButton = this.session.screen.backButton;
    }

    super.ngDoCheck();
  }

}
