import { SessionService } from '../services/session.service';
import { AbstractApp } from '../common/abstract-app';
import { AbstractTemplate } from '.';

export interface IScreen {
    show(screen: any, app: AbstractApp, template?: AbstractTemplate): void;
}
