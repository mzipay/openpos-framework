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
import { IConfirmationDialog } from '../interfaces/confirmation-dialog.interface';

export const DEFAULT_LOCALE = 'en-US';
@Injectable({
    providedIn: 'root',
  })
export class PersonalizationService {

    private serverBaseUrl: string;

    private previousTheme: string;

    private theme: string;

    public onThemeChanging = new EventEmitter<IThemeChangingEvent>();

    constructor(private location: Location, private router: Router) {
    }

    public personalize(serverName: string, serverPort: string, node: string | {storeId: string, deviceId: string},
        sslEnabled?: boolean, refreshApp: boolean = true) {

        let nodeId = '';
        if (typeof node === 'string') {
            nodeId = node;
        } else {
            nodeId = node.storeId + '-' + node.deviceId;
        }
        console.log(`personalizing with server: ${serverName}, port: ${serverPort}, nodeid: ${nodeId}`);
        localStorage.setItem('serverName', serverName);
        localStorage.setItem('serverPort', serverPort);
        localStorage.setItem('nodeId', nodeId);
        if (sslEnabled) {
            localStorage.setItem('sslEnabled', '' + sslEnabled);
        } else {
            localStorage.setItem('sslEnabled', 'false');
        }
        this.serverBaseUrl = null; // will be regenerated on next fetch
        if (refreshApp) {
            this.refreshApp();
        }

    }

    public dePersonalize() {
        const theme = this.getTheme();
        localStorage.removeItem('serverName');
        localStorage.removeItem('serverPort');
        localStorage.removeItem('nodeId');
        localStorage.removeItem('theme');
        localStorage.removeItem('sslEnabled');
        this.setTheme(theme,  true);
    }

    public getWebsocketUrl(): string {
        let protocol = 'ws://';
        if (this.isSslEnabled()) {
            protocol = 'wss://';
        }
        let url: string = protocol + this.getServerName();
        if (this.getServerPort()) {
            url = url + ':' + this.getServerPort();
        }
        url = url + '/api/websocket';
        return url;
    }

    public getPersonalizationScreen(): any {
        // tslint:disable-next-line:max-line-length
        return { type: 'Personalization', sequenceNumber: Math.floor(Math.random() * 2000), name: 'Device Setup', refreshAlways: true, template: { type: 'Blank', dialog: false } };
    }

    public getTheme(): string {
        const localTheme = localStorage.getItem('theme');
        if (this.theme) {
            return  this.theme;
        } else if (localTheme) {
            return localTheme;
        } else {
            return 'openpos-theme';
        }
    }

    public isSslEnabled(): boolean {
        return 'true' === localStorage.getItem('sslEnabled');
    }

    public setSslEnabled(enabled: boolean) {
        localStorage.setItem('sslEnabled', enabled + '');
    }

    public setTheme(theme: string, storeLocal: boolean) {
        localStorage.setItem('theme', theme);
        if (this.previousTheme !== theme) {
            console.log(`Theme changing from '${this.previousTheme}' to '${theme}'`);
            this.onThemeChanging.emit({currentTheme: this.previousTheme, newTheme: theme});
            this.previousTheme = theme;
        }
    }

    public setServerName(name: string) {
        localStorage.setItem('serverName', name);
    }

    public getServerName(): string {
        return localStorage.getItem('serverName');
    }

    public getDeviceName(): string {
        return localStorage.getItem('deviceName');
    }

    public setDeviceName(deviceName: string): void {
        localStorage.setItem('deviceName', deviceName);
    }

    public setServerPort(port: string) {
        localStorage.setItem('serverPort', port);
    }

    public getServerPort(): string {
        return localStorage.getItem('serverPort');
    }

    public getNodeId(): string {
        return localStorage.getItem('nodeId');
    }

    public setNodeId(id: string) {
        localStorage.setItem('nodeId', id);
    }

    public refreshApp() {
        window.location.href = 'index.html';
    }

    public isPersonalized(): boolean {
        if (this.getServerName() && this.getNodeId() && this.getServerPort()) {
            return true;
        } else {
            return false;
        }
    }

    public getServerBaseURL(): string {
        if (!this.serverBaseUrl) {
            const protocol = this.isSslEnabled() ? 'https' : 'http';
            this.serverBaseUrl = `${protocol}://${this.getServerName()}${this.getServerPort() ? `:${this.getServerPort()}` : ''}`;
            console.log(`Generated serverBaseURL: ${this.serverBaseUrl}`);
        }
        return this.serverBaseUrl;
    }

    public getApiServerBaseURL(): string {
        return `${this.getServerBaseURL()}/api`;
    }

}


