import { IPlugin } from './iplugin';

declare var cordova: any;

export class CordovaPlugin implements IPlugin {
    pluginId: string;
    pluginName?: string;
    impl: any;

    constructor(pluginId: string) {
        this.pluginId = pluginId;
        if (typeof cordova !== 'undefined') {
            if (cordova.plugins && cordova.plugins[pluginId]) {
                this.impl = cordova.plugins[pluginId];
                this.pluginName = this.impl.pluginName;
            } else if (cordova[pluginId]) {
                this.impl = cordova[pluginId];
                this.pluginName = pluginId;
            } else {
                console.log(`Unable to locate cordova '${pluginId}' plugin.`);
            }
        }
    }

    init(successCallback: () => void, errorCallback: (error?: string) => void): void {
        if (typeof this.impl.init !== 'undefined') {
            this.impl.init(successCallback, errorCallback);
        } else {
            successCallback();
        }
    }
}
