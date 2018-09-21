import { Logger } from './../../core/services/logger.service';
import { AppInjector } from '../../core/app-injector';
import { SessionService, IScreen, IAbstractScreen } from '../../core';

export abstract class PosScreen<T extends IAbstractScreen> implements IScreen {

    screen: T;
    session: SessionService;
    log: Logger;

    constructor() {
        this.session = AppInjector.Instance.get(SessionService);
        this.log = AppInjector.Instance.get(Logger);
    }

    show(screen: any) {
        this.screen = screen;
        this.buildScreen();
    }

    abstract buildScreen();
}
