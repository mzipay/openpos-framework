import { SessionService, AppInjector, IMenuItem, Logger } from '../../core';
import { OnDestroy, OnInit } from '@angular/core';
import { Subscription, Observable } from 'rxjs';
import { filter } from 'rxjs/operators';
import { deepAssign } from '../../utilites/';

export interface ScreenPartProps {
    name: string;
}

export function ScreenPartData( config: ScreenPartProps ) {
    return function<T extends {new(...args: any[]): {}}>(target: T) {
        return class extends target {
            screenPartName = config.name;
        };
    };
}


export abstract class ScreenPart<T> implements OnDestroy, OnInit {

    sessionService: SessionService;
    log: Logger;
    screenPartName: string;
    screenData: T;

    private subscription: Subscription;

    constructor() {
        this.sessionService = AppInjector.Instance.get(SessionService);
        this.log = AppInjector.Instance.get(Logger);

    }

    ngOnInit(): void {
        this.subscription = this.sessionService.getMessages('Screen')
            .pipe(filter( s => s.screenType !== 'Loading')).subscribe( s => {
            if ( s.hasOwnProperty(this.screenPartName)) {
                this.screenData = deepAssign( this.screenData, s[this.screenPartName]);
            } else {
                this.screenData = deepAssign( this.screenData, s );
            }
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
