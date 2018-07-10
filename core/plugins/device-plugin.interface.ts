
import { IDeviceRequest } from './device-request.interface';
import { IPlugin } from './plugin.interface';
export interface IDevicePlugin extends IPlugin {
    processRequest(deviceRequest: IDeviceRequest, successCallback: (response: any) => void, errorCallback: (error: string) => void);
}
