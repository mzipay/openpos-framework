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
                console.log(`plugin '${pluginId}' not found`);
                // handle future plugins here
            }
        }
        return plugin;
    }

    /*
     * Assumes plugin is in cordova.plugins data structure
     * See plugin.xml for the plugin at /plugin/platform/js-module/clobbers[@target]
     * @target should be set to location where the plugin can be accessed,
     * which needs to be cordova.plugins.xyz (where xyz is the pluginId) in order
     * for the plugin to be found.
     */
    isCordovaPlugin(pluginId: string): boolean {
        return typeof cordova !== 'undefined' && cordova.plugins[pluginId];
    }

}
