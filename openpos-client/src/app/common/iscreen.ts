import { SessionService } from '../services/session.service';
import { AbstractApp } from '../common/abstract-app';

export interface IScreen {
    show(session: SessionService, app: AbstractApp);
}
