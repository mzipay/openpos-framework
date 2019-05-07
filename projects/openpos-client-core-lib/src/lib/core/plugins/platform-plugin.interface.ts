import { Observable } from 'rxjs';

export interface IPlatformPlugin {
    name(): string;

    pluginPresent(): boolean;

    /// try and intialize the plugin and return True for success or False for failure
    initialize(): Observable<string>;
}
