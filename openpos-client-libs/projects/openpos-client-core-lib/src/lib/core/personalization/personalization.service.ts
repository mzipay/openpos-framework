import { Logger } from '../services/logger.service';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { PersonalizationResponse } from './personalization-response.interface';
import { DiscoveryResponse } from './discovery-response.interface';
import { timeout, catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class PersonalizationService {

    private serverBaseUrl: string;

    constructor(private log: Logger, private http: HttpClient) {
    }

    public personalize(
        serverName: string, serverPort: string, deviceId: string,
        personalizationProperties?: Map<string, string>, sslEnabled?: boolean) {

        this.log.info(`personalizing with server: ${serverName}, port: ${serverPort}, deviceId: ${deviceId}`);
        localStorage.setItem('serverName', serverName);
        localStorage.setItem('serverPort', serverPort);
        localStorage.setItem('deviceId', deviceId);
        this.setPersonalizationProperties(personalizationProperties);

        if (sslEnabled) {
            localStorage.setItem('sslEnabled', '' + sslEnabled);
        } else {
            localStorage.setItem('sslEnabled', 'false');
        }
        this.serverBaseUrl = null; // will be regenerated on next fetch
    }

    private setPersonalizationProperties(personalizationProperties?: Map<string, string>) {
        if (personalizationProperties) {
            const keys = Array.from(personalizationProperties.keys());

            localStorage.setItem('personalizationProperties', JSON.stringify(keys));

            for (const key of keys) {
                localStorage.setItem(key, personalizationProperties.get(key));
            }
        }
    }

    private removePersonalizationProperties() {
        const keyString = localStorage.getItem('personalizationProperties');
        if (keyString) {
            const keys: string[] = JSON.parse(keyString);
            if (keys) {
                for (const key of keys) {
                    localStorage.removeItem(key);
                }
            }
        }
        localStorage.removeItem('personalizationProperties');
    }

    public dePersonalize() {
        localStorage.removeItem('serverName');
        localStorage.removeItem('serverPort');
        localStorage.removeItem('deviceId');
        localStorage.removeItem('theme');
        this.removePersonalizationProperties();
        localStorage.removeItem('sslEnabled');
    }

    public getPersonalizationProperties(): Map<string, string> {
        const map = new Map<string, string>();
        const keyString = localStorage.getItem('personalizationProperties');
        if (keyString) {
            const keys: string[] = JSON.parse(keyString);
            if (keys) {
                for (const key of keys) {
                    const value = localStorage.getItem(key);
                    map.set(key, value);
                }
            }
        }
        return map;
    }

    public async getWebsocketUrl(): Promise<string> {
        if (this.isOpenposManagementServer()) {
            const response = await this.discoverDeviceProcess(
                this.getServerName(),
                this.getServerPort(),
                this.getDeviceId(),
                this.isSslEnabled()
            );
            if (response.success) {
                return this.isSslEnabled() ? response.secureWebSocketBaseUrl : response.webSocketBaseUrl;
            } else {
                this.log.warn(`Failed to reach OpenPOS Management Server at ${this.getServerName()}:${this.getServerPort()}. Reason: ${response.message}`);
                return null;
            }
        } else {
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
    }

    public isOpenposManagementServer(): boolean {
        return 'true' === localStorage.getItem('openposManagementServer');
    }

    public isSslEnabled(): boolean {
        return 'true' === localStorage.getItem('sslEnabled');
    }

    public setSslEnabled(enabled: boolean) {
        localStorage.setItem('sslEnabled', enabled + '');
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

    public getDeviceId(): string {
        return localStorage.getItem('deviceId');
    }

    public setDeviceId(id: string) {
        localStorage.setItem('deviceId', id);
    }

    public getPersonalizationResults(): string {
        return localStorage.getItem('personalizationResults');
    }

    public setPersonalizationResults(personalizationResults: string) {
        localStorage.setItem('personalizationResults', personalizationResults);
    }

    public refreshApp() {
        window.location.reload();
    }

    public isPersonalized(): boolean {
        if (this.getServerName() && this.getDeviceId() && this.getServerPort()) {
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

    public async discoverDeviceProcess(serverName: string, serverPort: string, deviceId: string, sslEnabled: boolean, appId?: string, maxWaitMillis=60000): Promise<DiscoveryResponse> {
        const url = `http${sslEnabled? 's' : ''}://${serverName}:${serverPort}/discover?deviceId=${deviceId}${appId ? '&appId=' + appId : ''}`;
        console.log('Discovering client using url: ' + url);

        let discoveryError: any = null;
        try {
            const httpResult = await this.http.get<DiscoveryResponse>(url, {})
//                .pipe(catchError(e => {
//                    return of({ success: false, message: e })
//                }))
                .pipe(timeout(maxWaitMillis), 
                    catchError(e => {
                        return of({ success: false, message: e.message })
                    })
                )
                .toPromise();
            if (httpResult) {
                if (httpResult.hasOwnProperty('success')) {
                    if (! httpResult.success) {
                        console.log('Discovery FAILED with url: ' + url);
                    }
                } else {
                    httpResult.success = true;
                }
                if (httpResult.success) {
                    console.log('Successful Discovery with url: ' + url);
                }
                return httpResult;
            } else {
                discoveryError = { success: false, message: '?' };
            }
        } catch (error) {
            discoveryError = error;
            discoveryError['success'] = false;
        }
    }

    public async requestPersonalization(serverName: string, serverPort: string, sslEnabled: boolean): Promise<PersonalizationResponse> {
        let url = sslEnabled ? 'https://' : 'http://';
        url += serverName + ':' + serverPort + '/personalize';

        console.log('Requesting Personalization with url: ' + url);

        let personalizeError: any = null;
        try {
            const httpResult = await this.http.get<PersonalizationResponse>(url, {}).toPromise();
            if (httpResult) {
                httpResult.success = true;
                console.log('Successful Personalization with url: ' + url);
                return httpResult;
            } else {
                personalizeError = { message: '?' };
            }
        } catch (error) {
            personalizeError = error;
        }

        if (personalizeError) {
            console.warn('bad validation of ' + url + ' with an error message of :' + personalizeError.message);
            return { success: false, message: personalizeError.message };
        }
    }
}


