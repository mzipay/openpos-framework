import { DynamicScreenComponent, AbstractTemplate } from '../components';

export interface IScreen {
    show(screen: any, app?: DynamicScreenComponent, template?: AbstractTemplate): void;
}
