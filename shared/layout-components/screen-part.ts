import { SessionService, AppInjector } from '../../core';

export abstract class ScreenPart<T> {

    sessionService: SessionService;
    screenData: T;

    constructor() {
        this.sessionService = AppInjector.Instance.get(SessionService);
        this.sessionService.getMessages('Screen').subscribe( s => {
            this.screenData = s;
            this.screenDataUpdated();
        });
    }

    abstract screenDataUpdated();
}
