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
        let plugin: IDevicePlugin = this.plugins[pluginId];

        if (! plugin) {
            if (this.isCordovaPlugin(pluginId)) {
                plugin = cordova.plugins[pluginId];
                console.log(`Initializing plugin '${pluginId}'...`);
                this.pluginInit(plugin).then(
                  () => {
                    this.plugins[pluginId] = plugin;
                    console.log(`plugin '${pluginId}' initialized`);
                  }
                ).catch(
                    () => {
                      plugin = null;
                      console.log(`plugin '${pluginId}' failed to initialize`);
                    }
                );
            } else {
                console.log(`plugin '${pluginId}' not found`);
                // handle future plugins here
            }
        }
        return plugin;
    }

    private pluginInit(plugin: IPlugin): Promise<IPlugin> {
        const returnPromise: Promise<IPlugin> = new Promise(
          (resolve, reject) => {
            plugin.init(
                () => {
                    resolve();
                },
                () => {
                    reject();
                }
            );
          }
        );

        return returnPromise;
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
