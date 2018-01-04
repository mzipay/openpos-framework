import { IPlugin } from './iplugin';

declare var cordova: any;

export class CordovaPlugin implements IPlugin {
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
}
