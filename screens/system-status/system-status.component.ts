import { ISystemStatusScreen } from './system-status-screen.interface';
import { PosScreen } from '../pos-screen/pos-screen.component';
import { IDevice } from '../../core/interfaces/device.interface';
import { Component } from '@angular/core';

@Component({
    selector: 'app-system-status',
    templateUrl: './system-status.component.html',
    styleUrls: ['./system-status.component.scss']
})
export class SystemStatusComponent extends PosScreen<ISystemStatusScreen> {

    devices: IDevice[];
    deviceHeader: string;
    statusHeader: string;

    buildScreen() {
        this.devices = this.screen.devices;
        this.deviceHeader = this.screen.deviceHeader;
        this.statusHeader = this.screen.statusHeader;
    }

    getOnlineString(device: IDevice): string {
        if (device.online) {
            return 'Online';
        }
        return 'Offline';
    }

}
