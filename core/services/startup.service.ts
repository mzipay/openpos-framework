import { Logger } from './logger.service';
import { concat, throwError, Observable, Subject, ReplaySubject } from 'rxjs';
import { Injectable, InjectionToken, Inject } from '@angular/core';
import {
    IStartupTask
} from '../interfaces';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, ActivatedRoute } from '@angular/router';
import { map, catchError } from 'rxjs/operators';
import { MatDialog } from '@angular/material';
import { ComponentType } from '@angular/cdk/overlay/index';

export const STARTUP_TASKS = new InjectionToken<IStartupTask[]>('Startup Tasks');
export const STARTUP_COMPONENT = new InjectionToken<ComponentType<any>>('Startup Component');
export const STARTUP_FAILED_COMPONENT = new InjectionToken<ComponentType<any>>('Startup Failed Component');

@Injectable({
    providedIn: 'root',
})
export class StartupService implements CanActivate {

    private dedupedTasks = new Map<string, IStartupTask>();

    public startupTaskMessages$ = new ReplaySubject<string>();

    constructor(
        @Inject(STARTUP_TASKS) tasks: Array<IStartupTask>,
        @Inject(STARTUP_COMPONENT) private startupComponent: ComponentType<any>,
        @Inject(STARTUP_FAILED_COMPONENT) private startupFailedComponent: ComponentType<any>,
        private matDialog: MatDialog,
        private log: Logger,
        protected router: Router,
        protected route: ActivatedRoute) {

        // This might not be the best way but it's the best I could come up with for now.
        // This allows task defined in the core module to be overriden by vendor specific modules
        // for example overriding the personalization task
        tasks.forEach( task => this.dedupedTasks.set(task.name, task));
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {

        const startupDialogRef = this.matDialog.open(
            this.startupComponent,
            {
                disableClose: true,
                hasBackdrop: false
            });

        const list = Array.from(this.dedupedTasks.values());
        list.sort((a, b) => a.order - b.order );

        // Get an array of task observables and attach task name to messages and errors
        const tasks = list.map( task => task.execute( { route, state }).pipe(
            map( message => `${task.name}: ${message}`),
            catchError( error => throwError(`${task.name}: ${error}`))) );

        const allMessages = [];

        // Run all tasks in order
        return Observable.create( (result: Subject<boolean>) => {
            concat(...tasks).subscribe(
                {
                    next: (message) => {
                        this.startupTaskMessages$.next(message);
                        this.log.info(message);
                        allMessages.push(message);
                    },
                    error: (error) => {
                        this.log.info(error);
                        result.next(false);
                        startupDialogRef.close();
                        this.matDialog.open(
                            this.startupFailedComponent, {
                                disableClose: true,
                                hasBackdrop: false,
                                data: {
                                    error: error,
                                    messages: allMessages
                                }
                            }).afterClosed().subscribe( () => location.reload() );
                    },
                    complete: () => {
                        this.log.info('Task completed');
                        result.next(true);
                        startupDialogRef.close();
                    }
                }
            );
        });
    }
}
