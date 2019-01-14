import { SessionService, AppInjector, IMenuItem } from '../../core';
import { OnDestroy } from '@angular/core';
import { Subscription, Observable } from 'rxjs';
import { filter } from 'rxjs/operators';
import { deepAssign } from '../../utilites/';
export abstract class ScreenPart<T> implements OnDestroy {

    sessionService: SessionService;
    screenData = {} as T;

    private subscription: Subscription;

    constructor() {
        this.sessionService = AppInjector.Instance.get(SessionService);
        this.subscription = this.sessionService.getMessages('Screen')
            .pipe(filter( s => s.screenType !== 'Loading')).subscribe( s => {
            this.screenData = deepAssign( this.screenData, s );
            this.screenDataUpdated();
        });
    }

    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }

    onMenuItemClick( menuItem: IMenuItem) {
        if (menuItem.enabled) {
            this.sessionService.onAction( menuItem );
        }
    }

    isActionDisabled( action: string): Observable<boolean> {
        return this.sessionService.actionIsDisabled(action);
    }

    abstract screenDataUpdated();
}
