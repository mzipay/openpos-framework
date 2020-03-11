import { ElectronService } from 'ngx-electron';
import { Logger } from './logger.service';
import { TestBed } from '@angular/core/testing';
import { SessionService, OpenposStompService, ImmediateLoadingMessage } from './session.service';
import { PersonalizationService } from '../personalization/personalization.service';
import { MatDialog } from '@angular/material';
import { DeviceService } from './device.service';
import { AppInjector } from '../app-injector';
import { Injector } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { DiscoveryService } from '../discovery/discovery.service';
import { Observable, BehaviorSubject } from 'rxjs';
import { Message } from '@stomp/stompjs';
import { StompState } from '@stomp/ng2-stompjs';


describe('SessionService', () => {

    let stompServiceSpy: jasmine.SpyObj<OpenposStompService>;
    let sessionService: SessionService;
    let deviceServiceSpy: jasmine.SpyObj<DeviceService>;
    let loggerServiceSpy: jasmine.SpyObj<Logger>;
    let discoveryServiceSpy: jasmine.SpyObj<DiscoveryService>;
    let personalizationServiceSpy: jasmine.SpyObj<PersonalizationService>;

    beforeEach(() => {
        const stompSpy = jasmine.createSpyObj('OpenposStompService',
            ['publish', 'initAndConnect', 'connected', 'subscribe', 'disconnect']);
        const personalizationSpy = jasmine.createSpyObj('PersonalizationService',
            ['getDeviceId', 'isManagedServer', 'getPersonalizationProperties']);
        const matDialogSpy = jasmine.createSpyObj('MatDialog', ['open']);
        const deviceSpy = jasmine.createSpyObj('DeviceService', ['isRunningInCordova']);
        const loggerSpy = jasmine.createSpyObj('Logger', ['debug', 'info', 'error']);
        const electronSpy = jasmine.createSpyObj('ElectronService', ['isElectronApp']);
        const discoverySpy = jasmine.createSpyObj('DiscoveryService',
            ['discoverDeviceProcess', 'getWebsocketUrl', 'clearCachedUrls', 'isManagementServerAlive']);
        electronSpy.isElectronApp = false;

        TestBed.configureTestingModule({
            imports: [
                HttpClientModule
              ],
            providers: [
                { provide: PersonalizationService, useValue: personalizationSpy },
                SessionService,
                { provide: MatDialog, useValue: matDialogSpy },
                { provide: OpenposStompService, useValue: stompSpy },
                { provide: Logger, useValue: loggerSpy },
                { provide: DeviceService, useValue: deviceSpy },
                { provide: ElectronService, useValue: electronSpy },
                { provide: DiscoveryService, useValue: discoverySpy },
            ]
        });

        AppInjector.Instance = TestBed.get(Injector);
        deviceServiceSpy = TestBed.get(DeviceService);

        loggerServiceSpy = TestBed.get(Logger);

        stompServiceSpy = TestBed.get(OpenposStompService);
        discoveryServiceSpy = TestBed.get(DiscoveryService);
        personalizationServiceSpy = TestBed.get(PersonalizationService);
        sessionService = TestBed.get(SessionService);

        sessionService.setAppId('pos');
        deviceServiceSpy.isRunningInCordova.and.returnValue(false);
        personalizationServiceSpy.isManagedServer.and.returnValue(false);
        personalizationServiceSpy.getPersonalizationProperties.and.returnValue(null);
        personalizationServiceSpy.getDeviceId.and.returnValue('00000-000');
        stompServiceSpy.connected.and.returnValue(true);
        stompServiceSpy.subscribe.and.returnValue(new Observable<Message>());
        stompServiceSpy.state = new BehaviorSubject<StompState>(null);
        discoveryServiceSpy.getWebsocketUrl.and.returnValue('ws://server:6140');

    });

    describe('onAction', () => {
        beforeEach(() => {
            stompServiceSpy.publish.and.returnValue(true);
        });
        it('should call StompService.publish when called', async () => {
            await sessionService.onAction('foo', 'bar', null, null);
            expect(stompServiceSpy.publish).toHaveBeenCalled();
        });
    });

    describe('subscribe', () => {
        beforeEach(() => {
        });

        it('should not invoke DiscoveryService.discoverDeviceProcess when not connected to a managed server', async () => {
            await sessionService.subscribe();
            expect(personalizationServiceSpy.isManagedServer).toHaveBeenCalledTimes(1);
            expect(discoveryServiceSpy.discoverDeviceProcess).toHaveBeenCalledTimes(0);
            expect(discoveryServiceSpy.getWebsocketUrl).toHaveBeenCalledTimes(1);
        });

        it('should invoke DiscoveryService.discoverDeviceProcess when server url has not yet been established', async () => {
            personalizationServiceSpy.isManagedServer.and.returnValue(true);
            discoveryServiceSpy.getWebsocketUrl.and.returnValues(
                null,
                'ws://server:6140'
            );
            discoveryServiceSpy.discoverDeviceProcess.and.returnValue({
                success: true,
                host: 'server',
                port: 6140,
                webSocketBaseUrl: 'ws://server:6140',
                secureWebSocketBaseUrl: 'wss://server:6140',
                webServiceBaseUrl: 'http://server:6140',
                secureWebServiceBaseUrl: 'https://server:6140'
            });

            await sessionService.subscribe();
            expect(personalizationServiceSpy.isManagedServer).toHaveBeenCalledTimes(1);
            expect(discoveryServiceSpy.getWebsocketUrl).toHaveBeenCalledTimes(2);
            expect(discoveryServiceSpy.getWebsocketUrl.calls.all()[0].returnValue).toEqual(null);
            expect(discoveryServiceSpy.getWebsocketUrl.calls.all()[1].returnValue).toEqual('ws://server:6140');
            expect(discoveryServiceSpy.discoverDeviceProcess).toHaveBeenCalledTimes(1);
            expect(stompServiceSpy.config.url).toBe('ws://server:6140');
            expect(stompServiceSpy.initAndConnect).toHaveBeenCalledTimes(1);
        });

        it('should not invoke DiscoveryService.discoverDeviceProcess when server url has already been established', async () => {
            personalizationServiceSpy.isManagedServer.and.returnValue(true);

            await sessionService.subscribe();
            expect(personalizationServiceSpy.isManagedServer).toHaveBeenCalledTimes(1);
            expect(discoveryServiceSpy.getWebsocketUrl).toHaveBeenCalled();
            expect(discoveryServiceSpy.discoverDeviceProcess).toHaveBeenCalledTimes(0);
            expect(stompServiceSpy.config.url).toBe('ws://server:6140');
            expect(stompServiceSpy.initAndConnect).toHaveBeenCalledTimes(1);
        });

    });

    describe('Stomp Connection Lost', () => {
        let sendMessageSpy = null;

        beforeEach(async () => {
            sendMessageSpy = spyOn(sessionService, 'sendMessage');
            await sessionService.subscribe();
            stompServiceSpy.state.next(StompState.CLOSED);
        });

        it('should send a Loading message when disconnected', async () => {
            expect(sendMessageSpy).toHaveBeenCalledTimes(1);
            expect(sendMessageSpy.calls.mostRecent().args[0] instanceof ImmediateLoadingMessage).toBeTruthy();
        });

    });

    describe('Stomp Connection Lost with Managed Server', () => {
        let sendMessageSpy = null;

        beforeEach(async () => {
            sendMessageSpy = spyOn(sessionService, 'sendMessage');
            personalizationServiceSpy.isManagedServer.and.returnValue(true);
            discoveryServiceSpy.isManagementServerAlive.and.returnValue(Promise.resolve(true));
            await sessionService.subscribe();
            stompServiceSpy.state.next(StompState.CLOSED);
        });

        it('should invoke Stomp disconnect when running with managed server', async () => {
            expect(stompServiceSpy.disconnect).toHaveBeenCalledTimes(1);
            expect(discoveryServiceSpy.clearCachedUrls).toHaveBeenCalledTimes(1);
        });

    });

});
