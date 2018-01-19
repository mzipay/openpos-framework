import { IPlugin } from './../common/iplugin';
import { IMenuItem } from '../common/imenuitem';
import { Component } from '@angular/core';
import { SessionService } from '../services/session.service';
import { PluginService } from './../services/plugin.service';

@Component({
  selector: 'app-statusbar',
  templateUrl: './statusbar.component.html'
})
export class StatusBarComponent {
  logFilenames: string[];
  logPlugin: IPlugin;
  protected devModeEnabled = false;
  protected clickTimes: number[] = [];

  constructor(public session: SessionService, public pluginService: PluginService) {
    this.devModeEnabled = localStorage.getItem('devMode') === 'true';
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

  protected onToolbarClick(): void  {
    if (this.clickTimes.length >= 5) {
      this.clickTimes.shift();
    }
    this.clickTimes.push((new Date()).getTime());

    let numClicksUnderThreshold = 0;
    for (let i = this.clickTimes.length - 1; i > 0; i--) {
      const t = this.clickTimes[i];
      const tPrev = this.clickTimes[i - 1];
      if (t - tPrev <= 1500) {
        numClicksUnderThreshold++;
      }
    }

    // console.log(`numClicksUnderThreshold: ${numClicksUnderThreshold}`);
    if (numClicksUnderThreshold >= 4) {
      this.clickTimes = [];
      this.devModeEnabled = (! this.devModeEnabled);
      localStorage.setItem('devMode', `${this.devModeEnabled}`);
    }
  }

  protected onDevMenuClick(): void {
    this.pluginService.getPlugin('openPOSCordovaLogPlugin').then(
      (plugin: IPlugin) => {
        this.logPlugin = plugin;
        if (this.logPlugin && this.logPlugin.impl) {
          this.logPlugin.impl.listLogFiles(
            (fileNames) => {
              this.logFilenames = fileNames;
            },
            (error) => {
              this.logFilenames = [];
            }
          );
        }
      }
    );
  }

  protected onDevRefreshView() {
    this.session.refreshApp();
  }

  protected onDevClearLocalStorage() {
    localStorage.clear();
  }

  protected onLogfileSelected(logFilename: string): void {
    if (this.logPlugin && this.logPlugin.impl) {
      this.logPlugin.impl.shareLogFile(
        logFilename,
        () => {
        },
        (error) => {
          console.log(error);
        }
      );
    }
  }


}
