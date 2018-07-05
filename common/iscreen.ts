import { DynamicScreenComponent } from './dynamic-screen/dynamic-screen.component';
import { AbstractTemplate } from '.';

export interface IScreen {
    show(screen: any, app?: DynamicScreenComponent, template?: AbstractTemplate): void;
}
