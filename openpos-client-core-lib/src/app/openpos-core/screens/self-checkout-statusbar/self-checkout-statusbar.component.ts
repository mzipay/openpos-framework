import { MatSnackBar, MatSnackBarVerticalPosition } from '@angular/material';
import { FileUploadService } from '../../services/file-upload.service';
import { IMenuItem } from '../../common/imenuitem';
import { Component } from '@angular/core';
import { SessionService } from '../../services/session.service';

@Component({
  selector: 'app-self-checkout-statusbar',
  templateUrl: './self-checkout-statusbar.component.html'
})
export class SelfCheckoutStatusBarComponent {

  constructor(private session: SessionService, public snackBar: MatSnackBar) {
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

}
