import { SessionService } from './../session.service';

export interface IScreen {
    show(session: SessionService);
}
