import { SessionService } from '../services/session.service';
import { AbstractApp } from '../common/abstract-app';

export interface IScreen {
    show(screen: any, app: AbstractApp): void;
}
