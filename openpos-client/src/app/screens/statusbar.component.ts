import { IMenuItem } from '../common/imenuitem';
import { Component } from '@angular/core';
import { SessionService } from '../services/session.service';
import { DeviceService } from '../services/device.service';

@Component({
  selector: 'app-statusbar',
  templateUrl: './statusbar.component.html'
})
export class StatusBarComponent {

  constructor(public session: SessionService, public devices: DeviceService) {
  }

  public doMenuItemAction(menuItem: IMenuItem) {
    if (menuItem.action === '<camera_scan>') {
      this.devices.cameraScan();
    } else {
      this.session.onAction(menuItem.action);
    }
  }

  public isMenuItemEnabled(m: IMenuItem): boolean {
    let enabled = m.enabled;
    if (m.action.startsWith('<') && this.session.isRunningInBrowser()) {
         enabled = false;
    }
    return enabled;
  }

}
