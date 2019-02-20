import { Logger } from './../../core/services/logger.service';
import { AppInjector } from '../../core/app-injector';
import { SessionService, IScreen, IAbstractScreen, IActionItem } from '../../core';
import { deepAssign } from '../../utilites';
import { QueryList, AfterViewInit, ViewChildren } from '@angular/core';
import { ScreenPartComponent } from '../../shared/screen-parts/screen-part';

export abstract class PosScreen<T extends IAbstractScreen> implements IScreen, AfterViewInit {

    screen: T;
    session: SessionService;
    log: Logger;

    @ViewChildren(ScreenPartComponent) screenParts: QueryList<ScreenPartComponent<any>>;

    constructor() {
        this.session = AppInjector.Instance.get(SessionService);
        this.log = AppInjector.Instance.get(Logger);
    }

    ngAfterViewInit(): void {
        this.updatePartTypes();
        this.screenParts.changes.subscribe( c => {
            this.updatePartTypes();
        });
    }

    private updatePartTypes() {
        this.screenParts.forEach( p => p.setMessageType(this.screen.type));
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
