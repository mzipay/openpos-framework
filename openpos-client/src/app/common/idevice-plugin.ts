import { IDeviceResponse } from './ideviceresponse';
import { Observable } from 'rxjs/Observable';
import { IDeviceRequest } from './idevicerequest';
import { IPlugin } from './iplugin';
export interface IDevicePlugin extends IPlugin {
    processRequest(requestSupplierCallback: () => any, successCallback: (response: string) => any, errorCallback: (error: string) => any);
}
