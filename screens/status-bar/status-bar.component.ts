import { MatSnackBar } from '@angular/material';
import { Component, Input } from '@angular/core';
import { StatusBarData } from './status-bar-data';
import { SessionService, PluginService, FileUploadService, IMenuItem } from '../../core';
@Component({
  selector: 'app-statusbar',
  templateUrl: './status-bar.component.html'
})
export class StatusBarComponent {
  @Input()
  data: StatusBarData;

  constructor(private session: SessionService,
    private pluginService: PluginService, private fileUploadService: FileUploadService,
    public snackBar: MatSnackBar) {
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
