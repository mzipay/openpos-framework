import { Injectable } from '@angular/core';
import { IStartupTask } from '../../interfaces/startup-task.interface';
import { Observable, Subject } from 'rxjs';
import { SessionService } from '../../services/session.service';
import { Router } from '@angular/router';
import { Logger } from '../../services/logger.service';
import { StartupTaskData } from './startup-task-data';
import { StartupTaskNames } from './startup-task-names';

@Injectable()
export class SubscribeToSessionTask implements IStartupTask {
    name = StartupTaskNames.SUBSCRIBE_TO_SESSION;
    order = 600;

    constructor(
        protected session: SessionService,
        protected router: Router,
        protected log: Logger
    ) {    }

    execute( data: StartupTaskData ): Observable<string> {
        return Observable.create( (message: Subject<string>) => {
            if (!this.session.connected()) {

                let appId = data.state.url.substring(1);
                this.log.info('calculating app id from ' + appId);
                if (appId.indexOf('?') > 0) {
                    appId = appId.substring(0, appId.indexOf('?'));
                }
                if (appId.indexOf('#') > 0) {
                    appId = appId.substring(0, appId.indexOf('#'));
                }
                if (appId.indexOf('/') > 0) {
                    appId = appId.substring(0, appId.indexOf('/'));
                }

                message.next(`[StartupService] Subscribing to server using appId '${appId}'...`);
                this.session.unsubscribe();
                this.session.subscribe(appId, data.route.queryParams);
                message.complete();
            }
        });
    }
}
