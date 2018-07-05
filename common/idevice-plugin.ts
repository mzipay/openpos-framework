
import { IDeviceRequest } from './idevicerequest';
import { IPlugin } from './iplugin';
export interface IDevicePlugin extends IPlugin {
    processRequest(deviceRequest: IDeviceRequest, successCallback: (response: any) => void, errorCallback: (error: string) => void);
}
