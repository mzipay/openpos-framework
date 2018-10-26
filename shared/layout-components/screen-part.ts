import { SessionService, AppInjector, IMenuItem } from '../../core';
import { OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
export abstract class ScreenPart<T> implements OnDestroy {

    sessionService: SessionService;
    screenData: T;

    private subscription: Subscription;

    constructor() {
        this.sessionService = AppInjector.Instance.get(SessionService);
        this.subscription = this.sessionService.getMessages('Screen').subscribe( s => {
            this.screenData = s;
            this.screenDataUpdated();
        });
    }

    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }

    onMenuItemClick( menuItem: IMenuItem) {
        this.sessionService.onAction( menuItem );
    }

    abstract screenDataUpdated();
}
