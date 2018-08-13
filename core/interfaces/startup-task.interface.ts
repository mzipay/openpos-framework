import { StartupComponent } from './../components/startup/startup.component';

export interface IStartupTask {
    name: string;
    order: number;
    execute(startupComponent: StartupComponent): Promise<boolean>;
}
