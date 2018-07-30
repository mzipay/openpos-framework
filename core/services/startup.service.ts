
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
// Importing the ../components barrel causes a circular reference since dynamic-screen references back to here,
// so we will import those files directly
import { LoaderState } from '../components/loader/loader-state';
import { ConfirmationDialogComponent } from '../components/confirmation-dialog/confirmation-dialog.component';
import { IDeviceResponse, IDeviceRequest } from '../plugins';
import {
    IMenuItem,
    IUrlMenuItem,
    IToastScreen,
    ToastType,
    Element,
    ActionMap
} from '../interfaces';

export const DEFAULT_LOCALE = 'en-US';
@Injectable({
    providedIn: 'root',
  })
export class StartupService {
}
