import { AppInjector } from '../../core/app-injector';
import { SessionService, IScreen } from '../../core';

export abstract class PosScreen<T> implements IScreen {
    screen: T;
    session: SessionService;
    constructor() {
        this.session = AppInjector.Instance.get(SessionService);
    }

    show(screen: any) {
        this.screen = screen;
        this.buildScreen();
    }

    abstract buildScreen();
}
