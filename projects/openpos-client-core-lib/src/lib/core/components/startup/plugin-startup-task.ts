import { Observable, merge, concat, of, Subject, iif } from 'rxjs';
import { IStartupTask } from '../../interfaces/startup-task.interface';
import { StartupTaskNames } from './startup-task-names';
import { InjectionToken, Optional, Inject } from '@angular/core';
import { IPlatformPlugin } from '../../plugins/platform-plugin.interface';
import { SCANNERS } from '../../services/scanner.service';
import { IScanner } from '../../plugins/scanner.interface';

export const PLUGINS = new InjectionToken<IPlatformPlugin[]>('Plugins');

export class PluginStartupTask implements IStartupTask {
    name =  StartupTaskNames.PLUGIN_INIT;
    order = 800;

    constructor(
        @Optional() @Inject(PLUGINS) private plugins: Array<IPlatformPlugin>,
        @Optional() @Inject(SCANNERS) private scanners: Array<IScanner> ) {
    }

    execute(): Observable<string> {

        const checkPlugins = Observable.create( (messages: Subject<string>) => {
            // remove plugins that are not loaded
            const pluginsToRemove = [];

            messages.next(`${this.plugins.length} plugins found to try`);
            this.plugins.forEach( p => {
                if ( !p.pluginPresent() ) {
                    pluginsToRemove.push(p);
                    messages.next(`Removing ${p.name()}`);
                } else {
                    messages.next(`Found ${p.name}`);
                }
            });

            pluginsToRemove.forEach( p => {
                this.plugins.splice( this.plugins.indexOf(p), 1);
                if ( this.scanners.includes(p)) {
                    this.scanners.splice( this.scanners.indexOf(p), 1);
                }
            });
            messages.complete();
        }) as Observable<string>;

        return concat(
            checkPlugins,
            iif( () => !!this.plugins && this.plugins.length > 0,
                concat (
                    of('Initializing plugins'),
                    merge( ...this.plugins.map(p => p.initialize()))
                ),
                of('No Plugins found')
            )
        );
    }
}
