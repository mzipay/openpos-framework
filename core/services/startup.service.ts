import { SessionService } from './session.service';
import { StartupComponent } from './../components/startup/startup.component';
import { Injectable, EventEmitter, NgZone } from '@angular/core';
import {
    IStartupTask
} from '../interfaces';
import { Router } from '@angular/router';
import { PersonalizationService } from './personalization.service';

@Injectable({
    providedIn: 'root',
})
export class StartupService {

    private tasks = new Map<string, IStartupTask>();

    constructor(private personalization: PersonalizationService, private session: SessionService, protected router: Router) {
    }

    public addStartupTask(task: IStartupTask): void {
        this.tasks.set(task.name, task);
    }

    public runTasks(startupComponent: StartupComponent): void {
        const list = Array.from(this.tasks);
        list.sort((a, b) => a[1].order - b[1].order );
        list.every(element => {
           const task = element[1];
           return task.execute(startupComponent);
        });

        if (this.personalization.isPersonalized()) {
            this.session.unsubscribe();
            this.session.subscribe(this.normalizeAppIdFromUrl());
        }
    }
    protected normalizeAppIdFromUrl(): string {
        let appId = this.router.url.substring(1);
        if (appId.indexOf('#') > 0) {
            appId = appId.substring(0, appId.indexOf('#'));
        }
        if (appId.indexOf('/') > 0) {
            appId = appId.substring(0, appId.indexOf('/'));
        }
        return appId;
    }
}
