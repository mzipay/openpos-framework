import { IPlugin } from './../../common/iplugin';
import { PluginService } from './../../services/plugin.service';
import { IMenuItem } from './../../common/imenuitem';
import { Component, OnInit } from '@angular/core';
import { IScreen } from '../../common';

declare var version: any; // read in from core at assets/version.js

@Component({
    selector: 'app-version',
    templateUrl: './version.component.html',
    styleUrls: ['./version.component.scss']
})
export class VersionComponent implements IScreen, OnInit {

    screen: any;
    versions: { id: string, name: string, version: string }[];
    primaryAction: IMenuItem;
    otherActions: IMenuItem[];

    constructor(private pluginService: PluginService) {

    }

    ngOnInit(): void {
    }

    show(screen: any): void {
        this.screen = screen;
        this.versions = this.screen.versions;

        const clientBuildVersion = {
            id: 'clientBuildVersion', name: 'Client Build Version',
            version: this.buildVersion
        };
        this.versions.unshift(clientBuildVersion);

        this.appVersion.then(v => {
            if (v !== 'n/a') {
                this.versions.unshift({
                    id: 'cordovaAppVersion', name: 'Cordova App Version',
                    version: v
                });
            }
        });

        if (screen.localMenuItems) {
            this.primaryAction = screen.localMenuItems[0];
        }

        if (screen.localMenuItems && screen.localMenuItems.length > 1) {
            this.otherActions = screen.localMenuItems.slice(1, screen.localMenuItems.length);
        }
    }

    protected get buildVersion(): string {
        return typeof version === 'undefined' ? 'unknown' : version;
    }

    public get appVersion(): Promise<string> {
        const promiseReturn = new Promise<string>(
            (resolve, reject) => {
                this.pluginService.getPlugin('openPOSCordovaLogPlugin').then(
                    (plugin: IPlugin) => {
                        if (plugin.impl) {
                            plugin.impl.getAppVersion(
                                (appVersion) => {
                                    resolve(appVersion);
                                },
                                (error) => {
                                    reject(`Failed to get client app version.  Reason: ${error}`);
                                }
                            );
                        } else {
                            reject(`Failed to get client app version.  Reason: logging plugin not loaded`);
                        }
                    }
                ).catch(error => {
                    reject(`Couldn't get client app version, not available. Reason: ${error}`);
                });
            });
        return promiseReturn;
    }
}
