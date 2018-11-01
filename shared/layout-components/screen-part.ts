import { SessionService, AppInjector, IMenuItem } from '../../core';
import { OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
export abstract class ScreenPart<T> implements OnDestroy {

    sessionService: SessionService;
    screenData: T;

    private subscription: Subscription;

    constructor() {
        this.sessionService = AppInjector.Instance.get(SessionService);
        this.subscription = this.sessionService.getMessages('Screen')
            .pipe(filter( s => (!s.template || !s.template.dialog) && s.screenType !== 'Loading')).subscribe( s => {
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
