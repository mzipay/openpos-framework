import { DynamicScreenComponent } from '../core';
import { AbstractTemplate } from './abstract-template';

export interface IScreen {
    show(screen: any, app?: DynamicScreenComponent, template?: AbstractTemplate): void;
}
