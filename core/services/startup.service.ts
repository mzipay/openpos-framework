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
    private _startupFailureTask: IStartupTask;

    onStartupCompleted = new BehaviorSubject<StartupStatus>(StartupStatus.Starting);

    constructor(private personalization: PersonalizationService, private session: SessionService, protected router: Router) {
    }

    set startupFailureTask(task: IStartupTask) {
        this._startupFailureTask = task;
    }

    get startupFailureTask(): IStartupTask {
        return this._startupFailureTask;
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
            this.handleStartupTaskResult(<boolean> allSuccess, startupComponent);
        }).catch(error => {
            console.log(`One or more startup tasks failed. Reason: ${error}`);
            this.handleStartupTaskResult(false, startupComponent);
        });

        // Note that this will run before the startup tasks have finished.  May want to
        // rethink that at some point.  Doesn't seem to hurt in failure scenarios I have
        // tested however and it allows for startup tasks to check the connection.
        this.doSubscription(startupComponent);
    }

    private handleStartupTaskResult(startupTasksSuccessful: boolean, startupComponent: StartupComponent) {
        if (!startupTasksSuccessful && this.startupFailureTask) {
            console.log(`${this.startupFailureTask.name} startup failure handler task is executing...`);
            this.startupFailureTask.execute(startupComponent).then(failureTaskResult => {
                console.log(`${this.startupFailureTask.name} startup failure handler task ${failureTaskResult ? 'succeeded' : 'failed'}.`);
                this.onStartupCompleted.next(startupTasksSuccessful ? StartupStatus.Success : StartupStatus.Failure);
            }).catch( error => {
                console.log(`${this.startupFailureTask.name} startup failure handler task failed'. Reason: ${error}`);
                this.onStartupCompleted.next(StartupStatus.Failure);
            });
        } else {
            this.onStartupCompleted.next(startupTasksSuccessful ? StartupStatus.Success : StartupStatus.Failure);
        }
    }

    private doSubscription(startupComponent: StartupComponent) {
        this.personalization.onPersonalized.subscribe(isPersonalized => {
            if (isPersonalized && ! this.session.connected()) {
                const appId = this.normalizeAppIdFromUrl();
                startupComponent.log(`[StartupService] Subscribing to server using appId '${appId}'...`);
                this.session.unsubscribe();
                this.session.subscribe(appId);
            }
        });
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
        console.log('calculating appid from ' + appId);
        if (appId.indexOf('?') > 0) {
            appId = appId.substring(0, appId.indexOf('?'));
        }
        if (appId.indexOf('#') > 0) {
            appId = appId.substring(0, appId.indexOf('#'));
        }
        if (appId.indexOf('/') > 0) {
            appId = appId.substring(0, appId.indexOf('/'));
        }
        return appId;
    }
}

export enum StartupStatus {
    Starting,
    Success,
    Failure
}
