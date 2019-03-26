import { OnDestroy, OnInit } from '@angular/core';
import { Subscription, Observable } from 'rxjs';
import { filter } from 'rxjs/operators';
import { MessageProvider } from '../providers/message.provider';
import { AppInjector } from '../../core/app-injector';
import { IActionItem } from '../../core/interfaces/menu-item.interface';
import { SessionService } from '../../core/services/session.service';
import { Logger } from '../../core/services/logger.service';
import { deepAssign } from '../../utilites/deep-assign';

export abstract class ScreenPartComponent<T> implements OnDestroy, OnInit {

    sessionService: SessionService;
    log: Logger;
    screenPartName: string;
    screenData: T;
    messageProvider: MessageProvider;
    private subscription: Subscription;

    constructor( messageProvider: MessageProvider ) {
        this.sessionService = AppInjector.Instance.get(SessionService);
        this.log = AppInjector.Instance.get(Logger);
        this.messageProvider = messageProvider;

    }

    ngOnInit(): void {
        this.subscription = this.messageProvider.getMessages$()
            .pipe(filter( s => s.screenType !== 'Loading' )).subscribe( s => {
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

    onMenuItemClick( menuItem: IActionItem, payload?: any ) {
        if (menuItem.enabled) {
            this.sessionService.onAction( menuItem, payload );
        }
    }

    isActionDisabled( action: string): Observable<boolean> {
        return this.sessionService.actionIsDisabled(action);
    }

    abstract screenDataUpdated();
}
