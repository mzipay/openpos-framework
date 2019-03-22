import { Logger } from './../../core/services/logger.service';
import { AppInjector } from '../../core/app-injector';
import { SessionService, IScreen, IAbstractScreen, IActionItem } from '../../core';
import { deepAssign } from '../../utilites';

export abstract class PosScreen<T extends IAbstractScreen> implements IScreen {

    screen: T;
    session: SessionService;
    log: Logger;

    constructor() {
        this.session = AppInjector.Instance.get(SessionService);
        this.log = AppInjector.Instance.get(Logger);
    }

    show(screen: any) {
        this.screen = deepAssign(this.screen, screen);
        this.buildScreen();
    }

    onMenuItemClick( menuItem: IActionItem, payload?: any) {
        if (menuItem.enabled) {
            this.session.onAction( menuItem, payload );
        }
    }

    abstract buildScreen();
}
