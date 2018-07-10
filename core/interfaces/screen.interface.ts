import { DynamicScreenComponent } from '../components/dynamic-screen/dynamic-screen.component';
import { AbstractTemplate } from '../components/abstract-template';


export interface IScreen {
    show(screen: any, app?: DynamicScreenComponent, template?: AbstractTemplate): void;
}
