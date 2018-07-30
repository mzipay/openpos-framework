import { Observable, Subscription, BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';
import { Message } from '@stomp/stompjs';
import { Injectable, EventEmitter, NgZone } from '@angular/core';
import { StompService, StompState } from '@stomp/ng2-stompjs';
import { Location } from '@angular/common';
import { MatDialog, MatSnackBar } from '@angular/material';
import { Router } from '@angular/router';
import { ActionIntercepter } from '../action-intercepter';
import { IThemeChangingEvent } from '../../shared/';
import { LoaderState } from '../components/loader/loader-state';
import { ConfirmationDialogComponent } from '../components/confirmation-dialog/confirmation-dialog.component';
import { IDeviceResponse, IDeviceRequest } from '../plugins';
import {
    IStartupTask
} from '../interfaces';

@Injectable({
    providedIn: 'root',
})
export class StartupService {

    private tasks = new Map<string, IStartupTask>();

    constructor() {
    }

    public addStartupTask(task: IStartupTask): void {
        this.tasks.set(task.name, task);
    }

    public runTasks(): void {
        const list = Array.from(this.tasks);
        list.sort((a, b) => a[1].order - b[1].order );
        list.forEach(element => {
           const task = element[1];
        });
    }
}
