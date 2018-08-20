import { BehaviorSubject } from 'rxjs';
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
    onStartupCompleted = new BehaviorSubject<StartupStatus>(StartupStatus.Starting);

    constructor(private personalization: PersonalizationService, private session: SessionService, protected router: Router) {
    }

    public addStartupTask(task: IStartupTask): void {
        this.tasks.set(task.name, task);
    }

    public runTasks(startupComponent: StartupComponent): void {
        const list = Array.from(this.tasks);
        list.sort((a, b) => a[1].order - b[1].order );

        // Run the tasks in order
        this.promiseLoop(
            list,
            // For the operation to execute, just run the task
            element => {
                const currentTask = element[1];
                startupComponent.log(`${currentTask.name} startup task is executing...`);
                const resultPromise = currentTask.execute(startupComponent);
                resultPromise.then(result => startupComponent.log(`${currentTask.name} startup task ${result ? 'succeeded' : 'failed'}.`));
                return resultPromise;
            },
            // The condition below will cause the task execution to stop
            // upon the first failed task.
            // taskResult will be null for first task due to way that
            // array.reduce() runs.
            taskResult => taskResult !== null && ! taskResult
        ).then(allSuccess => {
            console.log(`Result of running all tasks: ${allSuccess}`);
            this.onStartupCompleted.next(allSuccess ? StartupStatus.Success : StartupStatus.Failure);
        }).catch(error => {
            console.log(`One or more startup tasks failed. Reason: ${error}`);
            this.onStartupCompleted.next(StartupStatus.Failure)
        });

        if (this.personalization.isPersonalized()) {
            this.session.unsubscribe();
            this.session.subscribe(this.normalizeAppIdFromUrl());
        }


    }

    private promiseLoop<T, R>(
        array: T[],
        operation: (T) => Promise<R>,
        predicate: (R) => boolean = v => false,
        initial: R = null): Promise<R> {

        return array.reduce((promise: Promise<R>, current: T) => {
            return promise.then((value: R) => {
                if (predicate(value)) {
                    return promise;
                }
                return operation(current);
            });
        }, Promise.resolve(initial));
    }

    protected normalizeAppIdFromUrl(): string {
        let appId = this.router.url.substring(1);
        if (appId.indexOf('#') > 0) {
            appId = appId.substring(0, appId.indexOf('#'));
        }
        if (appId.indexOf('/') > 0) {
            appId = appId.substring(0, appId.indexOf('/'));
        }
        if (appId.indexOf('?') > 0) {
            appId = appId.substring(0, appId.indexOf('?'));
        }
        return appId;
    }
}

export enum StartupStatus {
    Starting,
    Success,
    Failure
}
