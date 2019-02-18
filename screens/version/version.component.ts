import { SessionService } from './../../core/services/session.service';
import { Component, OnInit } from '@angular/core';
import { PluginService, AppVersion, IActionItem } from '../../core';
import { PosScreen } from '../pos-screen/pos-screen.component';

declare var version: any; // read in from core at assets/version.js

@Component({
    selector: 'app-version',
    templateUrl: './version.component.html',
    styleUrls: ['./version.component.scss']
})
export class VersionComponent extends PosScreen<any> {

    versions: { id: string, name: string, version: string }[];
    primaryAction: IActionItem;
    otherActions: IActionItem[];

    constructor(public session: SessionService, private pluginService: PluginService, public appVersion: AppVersion) {
        super();
    }

    buildScreen(): void {
        this.versions = this.screen.versions;

        const clientBuildVersion = {
            id: 'clientBuildVersion', name: 'Client Build Version',
            version: this.appVersion.buildVersion()
        };
        this.versions.unshift(clientBuildVersion);

        this.appVersion.appVersion(this.pluginService).then(v => {
            if (v !== 'n/a') {
                this.versions.unshift({
                    id: 'cordovaAppVersion', name: 'Native App Version',
                    version: v
                });
            }
        });

        if (this.screen.localMenuItems) {
            this.primaryAction = this.screen.localMenuItems[0];
        }

        if (this.screen.localMenuItems && this.screen.localMenuItems.length > 1) {
            this.otherActions = this.screen.localMenuItems.slice(1, this.screen.localMenuItems.length);
        }
    }

}
