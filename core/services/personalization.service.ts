import { Logger } from './logger.service';
import { Injectable, EventEmitter } from '@angular/core';
import { IThemeChangingEvent } from '../../shared/events';

@Injectable({
    providedIn: 'root',
  })
export class PersonalizationService {

    private serverBaseUrl: string;

    private previousTheme: string;

    private theme: string;

    public onThemeChanging = new EventEmitter<IThemeChangingEvent>();

    constructor(private log: Logger ) {
    }

    public personalize(serverName: string, serverPort: string, node: string | {storeId: string, deviceId: string},
        deviceType: string, brandId: string, sslEnabled?: boolean) {

        let nodeId = '';
        if (typeof node === 'string') {
            nodeId = node;
        } else {
            nodeId = node.storeId + '-' + node.deviceId;
        }
        this.log.info(`personalizing with server: ${serverName}, port: ${serverPort}, nodeid: ${nodeId}`);
        localStorage.setItem('serverName', serverName);
        localStorage.setItem('serverPort', serverPort);
        localStorage.setItem('nodeId', nodeId);
        localStorage.setItem('deviceType', deviceType);
        localStorage.setItem('brandId', brandId);
        if (sslEnabled) {
            localStorage.setItem('sslEnabled', '' + sslEnabled);
        } else {
            localStorage.setItem('sslEnabled', 'false');
        }
        this.serverBaseUrl = null; // will be regenerated on next fetch
    }

    public dePersonalize() {
        const theme = this.getTheme();
        localStorage.removeItem('serverName');
        localStorage.removeItem('serverPort');
        localStorage.removeItem('nodeId');
        localStorage.removeItem('theme');
        localStorage.removeItem('deviceType');
        localStorage.removeItem('brandId');
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
        this.theme = theme;
        if (storeLocal) {
            localStorage.setItem('theme', theme);
        }
        if (this.previousTheme !== theme) {
            this.log.info(`Theme changing from '${this.previousTheme}' to '${theme}'`);
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

    public getDeviceType(): string {
        return localStorage.getItem('deviceType');
    }

    public setDeviceType(deviceType: string) {
        localStorage.setItem('deviceType', deviceType);
    }

    public getBrandId(): string {
        return localStorage.getItem('brandId');
    }

    public setBrandId(brandId: string) {
        localStorage.setItem('brandId', brandId);
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
            this.log.info(`Generated serverBaseURL: ${this.serverBaseUrl}`);
        }
        return this.serverBaseUrl;
    }

    public getApiServerBaseURL(): string {
        return `${this.getServerBaseURL()}/api`;
    }

}


