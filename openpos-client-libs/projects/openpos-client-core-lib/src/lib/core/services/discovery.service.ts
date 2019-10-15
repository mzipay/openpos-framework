import { Injectable } from '@angular/core';
import { Logger } from './logger.service';
import { HttpClient } from '@angular/common/http';
import { PersonalizationService } from '../personalization/personalization.service';
import { DiscoveryResponse } from '../interfaces/discovery-response.interface';
import { timeout, catchError, map } from 'rxjs/operators';
import { of } from 'rxjs';
import { DiscoveryParams } from '../interfaces/discovery-params.interface';

@Injectable({
    providedIn: 'root',
})
export class DiscoveryService {

    private serverBaseUrl: string;
    private websocketUrl: string;

    constructor(protected personalization: PersonalizationService,
                private http: HttpClient, private log: Logger) {
    }

    public clearCachedUrls() {
        this.serverBaseUrl = null;
        this.websocketUrl = null;
    }

    public async isManagementServerAlive(): Promise<boolean> {
        let result = false;
        if (this.personalization.isManagedServer()) {
            const url = `http${this.personalization.isSslEnabled() ? 's' : ''}` +
                `://${this.personalization.getServerName()}:${this.personalization.getServerPort()}/ping`;
            const httpResult = await this.http.get(url, {responseType: 'text'})
                .pipe(timeout(1000),
                    catchError(e => {
                        return of(false);
                    })
                )
                .toPromise();
            result = httpResult != null;
        } else {
            this.log.warn(`Not a managed server`);
        }

        return result;
    }

    private makeParams(params?: DiscoveryParams): DiscoveryParams {
        return {
            server: params && typeof params.server !== 'undefined' ? params.server : this.personalization.getServerName(),
            port: params && typeof params.port !== 'undefined' ? params.port : this.personalization.getServerPort(),
            deviceId: params && typeof params.deviceId !== 'undefined' ? params.deviceId : this.personalization.getDeviceId(),
            sslEnabled: params && typeof params.sslEnabled !== 'undefined' ? params.sslEnabled : this.personalization.isSslEnabled(),
            appId: params && typeof params.appId !== 'undefined' ? params.appId : null,
            maxWaitMillis: params && typeof params.maxWaitMillis !== 'undefined' ? params.maxWaitMillis : 90000
        };
    }
    public async discoverDeviceProcess(parameters?: DiscoveryParams): Promise<DiscoveryResponse> {
        const params = this.makeParams(parameters);
        const url = `http${params.sslEnabled ? 's' : ''}://${params.server}:${params.port}/discover?` +
            `deviceId=${params.deviceId}${params.appId ? '&appId=' + params.appId : ''}`;
        this.log.info('Discovering device process using url: ' + url);

        let discoveryError: any = null;
        try {
            const httpResult = await this.http.get<DiscoveryResponse>(url, {})
                .pipe(timeout(params.maxWaitMillis),
                    catchError(e => {
                        return of({ success: false, message: e.message });
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
                    const discoveryResp = httpResult as DiscoveryResponse;
                    if (this.personalization.isSslEnabled()) {
                        this.serverBaseUrl = discoveryResp.secureWebServiceBaseUrl;
                        this.websocketUrl = discoveryResp.secureWebSocketBaseUrl;
                    } else {
                        this.serverBaseUrl = discoveryResp.webServiceBaseUrl;
                        this.websocketUrl = discoveryResp.webSocketBaseUrl;
                    }
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

    public getServerBaseURL(): string {
        if (!this.serverBaseUrl) {
            if (this.personalization.isManagedServer()) {
                this.log.debug(`serverBaseURL isn't set yet for the managed server`);
            } else {
                const protocol = this.personalization.isSslEnabled() ? 'https' : 'http';
                this.serverBaseUrl = `${protocol}://${this.personalization.getServerName()}` +
                    `${this.personalization.getServerPort() ? `:${this.personalization.getServerPort()}` : ''}`;
                this.log.info(`Generated serverBaseURL: ${this.serverBaseUrl}`);
            }
        }
        return this.serverBaseUrl;
    }

    public getApiServerBaseURL(): string {
        return `${this.getServerBaseURL()}/api`;
    }

    public getWebsocketUrl(): string {
        if (!this.websocketUrl) {
            if (this.personalization.isManagedServer()) {
                this.log.debug(`webSocketUrl isn't set yet for the managed server`);
            } else {
                let protocol = 'ws://';
                if (this.personalization.isSslEnabled()) {
                    protocol = 'wss://';
                }
                let url: string = protocol + this.personalization.getServerName();
                if (this.personalization.getServerPort()) {
                    url = url + ':' + this.personalization.getServerPort();
                }
                url = url + '/api/websocket';
                this.websocketUrl = url;
            }
        }

        return this.websocketUrl;
    }

}
