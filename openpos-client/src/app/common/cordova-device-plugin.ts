import { IDeviceRequest } from './idevicerequest';
import { IPlugin } from './iplugin';
import { IDevicePlugin } from './idevice-plugin';

declare var cordova: any;

export class CordovaDevicePlugin implements IDevicePlugin {
    pluginId: string;
    pluginName?: string;
    impl: any;

    constructor(pluginId: string) {
        this.pluginId = pluginId;
        if (typeof cordova !== 'undefined') {
            if (cordova.plugins[pluginId]) {
                this.impl = cordova.plugins[pluginId];
                this.pluginName = this.impl.pluginName;
            }
        }
    }

    init(successCallback: () => void, errorCallback: (error?: string) => void): void {
        this.impl.init(successCallback, errorCallback);
    }

    processRequest(request: IDeviceRequest, successCallback: (response: string) => any, errorCallback: (error: string) => any) {
        this.impl.processRequest(() => request.payload, successCallback, errorCallback);
    }
}
