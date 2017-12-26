import { IDevicePlugin } from './../common/idevice-plugin';
import { IPlugin } from './../common/iplugin';
import { Injectable } from '@angular/core';

declare var cordova: any;

@Injectable()
export class PluginService {

    private plugins = new Map<string, IPlugin>();

    constructor() {
    }

    getDevicePlugin(pluginId: string): IDevicePlugin {
        let plugin: IDevicePlugin = null;
        if (!this.plugins[pluginId]) {
            if (this.isCordovaPlugin(pluginId)) {
                plugin = cordova.plugins[pluginId];
                this.plugins[pluginId] = plugin;
                // TODO: handle exceptions during init
                plugin.init();
            } else {
                // future
            }
        }
        return plugin;
    }

    isCordovaPlugin(pluginId: string): boolean {
        return cordova.plugins[pluginId];
    }

}
