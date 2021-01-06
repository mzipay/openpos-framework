import { Observable, merge, concat, of, Subject, iif, from, Subscriber } from 'rxjs';
import { IStartupTask } from './startup-task.interface';
import { StartupTaskNames } from './startup-task-names';
import { InjectionToken, Optional, Inject, Injectable } from '@angular/core';
import { IPlatformPlugin } from '../platform-plugins/platform-plugin.interface';
import { SCANNERS } from '../platform-plugins/scanners/scanner.service';
import { IScanner } from '../platform-plugins/scanners/scanner.interface';
import { mergeAll, mergeMap, tap } from 'rxjs/operators';

export const PLUGINS = new InjectionToken<IPlatformPlugin[]>('Plugins');

/**
 * This startup task will find all registered plugins an check to see if they are loaded.
 * If the plug in is not loaded it will be removed from the PLATFORMS token otherwise this
 * task will wait for all plugins to initialize.
 */
@Injectable()
export class PluginStartupTask implements IStartupTask {
    name =  StartupTaskNames.PLUGIN_INIT;
    order = 800;

    constructor(
        @Optional() @Inject(PLUGINS) private plugins: Array<IPlatformPlugin>,
        @Optional() @Inject(SCANNERS) private scanners: Array<IScanner> ) {
    }

    execute(): Observable<string> {
        const checkPlugins = new Observable<string>(subscriber => {
            const pluginsToRemove = [];

            subscriber.next(`${this.plugins.length} plugins found to try`);
            
            this.plugins.forEach( p => {
                if ( !p.pluginPresent() ) {
                    pluginsToRemove.push(p);
                    subscriber.next(`Removing ${p.name()}`);
                } else {
                    subscriber.next(`Found ${p.name()}`);
                }
            });

            pluginsToRemove.forEach( p => {
                this.plugins.splice( this.plugins.indexOf(p), 1);
                if ( !!this.scanners && this.scanners.includes(p)) {
                    this.scanners.splice( this.scanners.indexOf(p), 1);
                }
            });

            subscriber.complete();
        });

        const pluginInitialization = from(this.plugins).pipe(
            mergeMap(plugin => concat(
                of(`Initializing ${plugin.name()}`),
                plugin.initialize()
            )),
        );

        return concat(
            checkPlugins,
            iif( () => !!this.plugins && this.plugins.length > 0,
                concat (
                    of('Initializing plugins'),
                    pluginInitialization,
                    of('Done initalizing plugins')
                ),
                of('No Plugins found')
            )
        );
    }
}
