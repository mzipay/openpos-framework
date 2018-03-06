import { IPlugin } from './../common/iplugin';
import { PluginService } from '../services/plugin.service';
import { FileUploadService } from './../services/file-upload.service';
import { DeviceService } from './../services/device.service';
import { ScreenService } from './../services/screen.service';
import { AbstractApp } from '../common/abstract-app';
import { IMenuItem } from '../common/imenuitem';
import { Component, DoCheck, ViewChild, NgZone, HostListener } from '@angular/core';
import { SessionService } from '../services/session.service';
import { FocusDirective } from '../common/focus.directive';
import { MatDialog, MatDialogRef, MatSnackBar, MatMenuTrigger } from '@angular/material';
import { IconService } from './../services/icon.service';
import { OverlayContainer } from '@angular/cdk/overlay';
import { Router } from '@angular/router';


@Component({
  selector: 'app-pos',
  templateUrl: './pos.component.html',
  styleUrls: ['./pos.component.scss']
})
export class PosComponent extends AbstractApp implements DoCheck {

  @ViewChild(MatMenuTrigger) devMenu: MatMenuTrigger;

  public backButton: IMenuItem;

  firstClickTime = Date.now();

  clickCount = 0;

  logFilenames: string[];

  logPlugin: IPlugin;

  constructor(public screenService: ScreenService, public session: SessionService,
    public deviceService: DeviceService, public dialog: MatDialog,
    public iconService: IconService, public snackBar: MatSnackBar, public overlayContainer: OverlayContainer,
    protected router: Router, public zone: NgZone, private pluginService: PluginService,
    private fileUploadService: FileUploadService) {

    super(screenService, session, dialog, iconService, snackBar, overlayContainer, router, zone);

  }

  @HostListener('document:click', ['$event'])
  @HostListener('document:touchstart', ['$event'])
  documentClick(event: MouseEvent) {
    if (this.clickCount === 0 || Date.now() - this.firstClickTime > 1000) {
      this.firstClickTime = Date.now();
      this.clickCount = 0;
    }
    this.clickCount = ++this.clickCount;

    if (this.clickCount === 5) {
      this.onDevMenuClick();
      this.clickCount = 0;
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
    this.session.loaderState.setEnabled(false);
    this.devMenu.openMenu();

    setTimeout(() => {
      if (this.devMenu.menuOpen) {
        this.session.loaderState.setEnabled(true);
        this.devMenu.closeMenu();
      }
    }, 10000);
  }

  protected onDevRefreshView() {
    this.session.refreshApp();
    this.devMenu.closeMenu();
  }

  protected onPersonalize() {
    this.session.dePersonalize();
    this.session.showScreen(this.session.getPersonalizationScreen());
    this.devMenu.closeMenu();
  }

  protected onDevClearLocalStorage() {
    localStorage.clear();
    this.devMenu.closeMenu();
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
    this.devMenu.closeMenu();
  }

  protected onLogfileUpload(logFilename: string): void {
    if (this.logPlugin && this.logPlugin.impl) {
      this.logPlugin.impl.getLogFilePath(
        logFilename,
        (logfilePath) => {
          this.fileUploadService.uploadLocalDeviceFileToServer('log', logFilename, 'text/plain', logfilePath)
            .then((result: { success: boolean, message: string }) => {
              // TODO: display a dialog with success.
              this.snackBar.open(result.message, 'Dismiss', {
                duration: 5000, verticalPosition: 'top'
              });
            })
            .catch((result: { success: boolean, message: string }) => {
              this.snackBar.open(result.message, 'Dismiss', {
                duration: 5000, verticalPosition: 'top'
              });
            });
        },
        (error) => {
          console.log(error);
        }
      );
    }
    this.devMenu.closeMenu();
  }

  // TODO should this come from the route name instead?
  public appName(): string {
    return 'pos';
  }

  ngDoCheck(): void {
    if (typeof this.session.screen !== 'undefined') {
      this.backButton = this.session.screen.backButton;
    }

    super.ngDoCheck();
  }

}
