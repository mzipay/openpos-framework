import { OnDestroy, OnInit } from '@angular/core';
import { Subscription, Observable } from 'rxjs';
import { filter } from 'rxjs/operators';
import { MessageProvider } from '../providers/message.provider';
import { AppInjector } from '../../core/app-injector';
import { IActionItem } from '../../core/interfaces/action-item.interface';
import { SessionService } from '../../core/services/session.service';
import { Logger } from '../../core/services/logger.service';
import { deepAssign } from '../../utilites/deep-assign';
import { OpenposMediaService } from '../../core/services/openpos-media.service';

export abstract class ScreenPartComponent<T> implements OnDestroy, OnInit {

    sessionService: SessionService;
    log: Logger;
    screenPartName: string;
    screenData: T;
    messageProvider: MessageProvider;
    mediaService: OpenposMediaService;
    isMobile$: Observable<boolean>;
    initialScreenType = '';
    public subscriptions = new Subscription();

    constructor(messageProvider: MessageProvider) {
        this.sessionService = AppInjector.Instance.get(SessionService);
        this.log = AppInjector.Instance.get(Logger);
        this.mediaService = AppInjector.Instance.get(OpenposMediaService);

        this.messageProvider = messageProvider;
        const sizeMap = new Map([
            ['xs', true],
            ['sm', false],
            ['md', false],
            ['lg', false],
            ['xl', false]
        ]);
        this.isMobile$ = this.mediaService.mediaObservableFromMap(sizeMap);

    }

    ngOnInit(): void {
        this.subscriptions.add(this.messageProvider.getMessages$()
            .pipe(filter(s => s.screenType !== 'Loading')).subscribe(s => {
                if (!this.initialScreenType.length) {
                    this.initialScreenType = s.screenType;
                }
                if (s.screenType === this.initialScreenType) {
                    if (s.hasOwnProperty(this.screenPartName)) {
                        this.screenData = deepAssign(this.screenData, s[this.screenPartName]);
                    } else {
                        this.screenData = deepAssign(this.screenData, s);
                    }
                    this.screenDataUpdated();
                }
            }));
    }
    ngOnDestroy(): void {
        this.subscriptions.unsubscribe();
    }

    onMenuItemClick(menuItem: IActionItem, payload?: any) {
        if (menuItem.enabled) {
            this.sessionService.onAction(menuItem, payload);
        }
    }

    isActionDisabled(action: string): Observable<boolean> {
        return this.sessionService.actionIsDisabled(action);
    }

    abstract screenDataUpdated();
}
