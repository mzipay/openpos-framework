import { SessionService, AppInjector, IActionItem, Logger } from '../../core';
import { OnDestroy, Component, forwardRef } from '@angular/core';
import { Subscription, Observable } from 'rxjs';
import { filter } from 'rxjs/operators';
import { deepAssign } from '../../utilites/';

export interface ScreenPartProps extends Component {
    name?: string;
}

export function ScreenPart( config: ScreenPartProps ) {

    return function<T extends {new(...args: any[]): {}}>(target: T) {
        const newClazz = class extends target {
            screenPartName = config.name;
            };
        const provider = {provide: ScreenPartComponent, useExisting: forwardRef(() => newClazz) };
        if ( !config.providers ) {
            config.providers = [provider];
        } else {
            config.providers.push(provider);
        }
        const ngCompDecorator = Component(config);
        ngCompDecorator(target);

        return newClazz;
    };
}

export abstract class ScreenPartComponent<T> implements OnDestroy {

    sessionService: SessionService;
    log: Logger;
    screenPartName: string;
    screenData: T;
    private subscription: Subscription;


    constructor() {
        this.sessionService = AppInjector.Instance.get(SessionService);
        this.log = AppInjector.Instance.get(Logger);

    }

    setMessageType( type: string) {
        this.subscription = this.sessionService.getMessages( type )
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

    onMenuItemClick( menuItem: IActionItem) {
        if (menuItem.enabled) {
            this.sessionService.onAction( menuItem );
        }
    }

    isActionDisabled( action: string): Observable<boolean> {
        return this.sessionService.actionIsDisabled(action);
    }

    abstract screenDataUpdated();
}
