import { IAbstractScreen } from '../../core/interfaces/abstract-screen.interface';
import { IDevice } from '../../core/interfaces/device.interface';

export interface ISystemStatusScreen extends IAbstractScreen {
    devices: IDevice[];
    deviceHeader: string;
    statusHeader: string;
}
