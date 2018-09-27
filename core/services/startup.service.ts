import { Logger } from './logger.service';
import { concat, throwError, Observable, Subject, ReplaySubject } from 'rxjs';
import { Injectable, InjectionToken, Inject, Optional } from '@angular/core';
import {
    IStartupTask
} from '../interfaces';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, ActivatedRoute } from '@angular/router';
import { map, catchError } from 'rxjs/operators';
import { MatDialog, MatDialogRef } from '@angular/material';
import { ComponentType } from '@angular/cdk/overlay/index';

export const STARTUP_TASKS = new InjectionToken<IStartupTask[]>('Startup Tasks');
export const STARTUP_FAILED_TASK = new InjectionToken<IStartupTask>('Startup Failed Task');
export const STARTUP_COMPONENT = new InjectionToken<ComponentType<any>>('Startup Component');
export const STARTUP_FAILED_COMPONENT = new InjectionToken<ComponentType<any>>('Startup Failed Component');

@Injectable({
    providedIn: 'root',
})
export class StartupService implements CanActivate {

    private dedupedTasks = new Map<string, IStartupTask>();

    public startupTaskMessages$ = new ReplaySubject<string>();

    private allMessages = [];
    private startupDialogRef: MatDialogRef<any>;

    constructor(
        @Inject(STARTUP_TASKS) tasks: Array<IStartupTask>,
        @Optional() @Inject(STARTUP_FAILED_TASK) private failedTask: IStartupTask,
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

        this.startupDialogRef = this.matDialog.open(
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


        // Run all tasks in order
        return Observable.create( (result: Subject<boolean>) => {
            concat(...tasks).subscribe(
                {
                    next: (message) => { this.handleMessage(message); },
                    error: (error) => {
                        result.next(false);
                        this.handleError( error );
                    },
                    complete: () => {
                        this.log.info('Task completed');
                        result.next(true);
                        this.startupDialogRef.close();
                    }
                }
            );
        });
    }

    private handleMessage( message: string ) {
        this.startupTaskMessages$.next(message);
        this.log.info(message);
        this.allMessages.push(message);
    }

    private handleError( error: string ) {
        this.log.info(error);
        if ( this.failedTask ) {
            this.failedTask.execute().subscribe(
                {
                    next: (message) => this.handleMessage(message),
                    complete: () => this.showFailure(error)
                }
            );
        } else {
            this.showFailure( error );
        }
    }

    private showFailure( error: string ){
        this.startupDialogRef.close();
        this.matDialog.open(
            this.startupFailedComponent, {
                disableClose: true,
                hasBackdrop: false,
                data: {
                    error: error,
                    messages: this.allMessages
                }
            }).afterClosed().subscribe( () => location.reload() );
    }
}
