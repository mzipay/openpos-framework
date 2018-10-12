import { Logger } from './logger.service';
import { IMessageHandler } from './../interfaces/message-handler.interface';
import { Injectable } from '@angular/core';
import { Subscription, BehaviorSubject, Subject } from 'rxjs';
import { FileUploadService } from './file-upload.service';
import {
    LogfileDownloadPlugin,
    InAppBrowserPlugin,
    BarcodeScannerPlugin,
    DEVICE_ERROR_RESPONSE_TYPE,
    DEVICE_RESPONSE_TYPE,
    DEVICE_DNE_RESPONSE_TYPE,
    IDevicePlugin,
    IDeviceRequest,
    Scan
 } from '../plugins';
import { PluginService } from './plugin.service';
import { SessionService } from './session.service';

declare var cordova: any;

@Injectable({
    providedIn: 'root',
  })
export class DeviceService implements IMessageHandler {

  public onDeviceReady: Subject<string> = new BehaviorSubject<string>(null);
  public onAppEnteringBackground: Subject<boolean> = new BehaviorSubject<boolean>(null);
  public onAppEnteringForeground: Subject<boolean> = new BehaviorSubject<boolean>(null);
  public onAppEnteredForeground: Subject<boolean> = new BehaviorSubject<boolean>(null);

  private screen: any;

  private screenSubscription: Subscription;
  private _isRunningInCordova: boolean = null;
  private cameraScanInProgress = false;

  constructor(private log: Logger, protected session: SessionService, public pluginService: PluginService, private fileUploadService: FileUploadService) {
    this.screenSubscription = this.session.subscribeForScreenUpdates((screen: any): void => this.screen = screen);
    // On iOS need to enter into loading state when the app is backgrounded, otherwise
    // user can execute actions as app is coming back to foreground.

    this.onAppEnteringBackground.subscribe(async(backgrounded) => {
        if (backgrounded) {
            const allowBackgroundHandling = !this.isCameraScanInProgress() && ! await this.isInAppBrowserActive();
            if (allowBackgroundHandling) {
                this.handleBackgrounding();
            } else {
                this.log.info(`Skipping background handling`);
            }
        }
    });

    this.onAppEnteredForeground.subscribe(foregrounded => {
        const allowForegroundHandling = this.session.inBackground;
        if (foregrounded) {
            if (allowForegroundHandling) {
                this.handleForegrounding();
            } else {
                this.log.info(`Skipping foreground handling`);
            }
        }
    });

    document.addEventListener('deviceready',
        () => {
            this.log.info('cordova devices are ready for the device service');
            this.initializeInAppBrowserPlugin();
            this.initializeBarcodeScannerPlugin();
            this.initializeLogfileDownloadPlugin();
            this._isRunningInCordova = true;
            this.onDeviceReady.next(`Application is initialized on platform '${cordova.platform}'`);
        },
        false
    );
    // For iOS in particular listen for the 'resign' event which is sent as we are either transitioning into the background
    // or resiging status as the active app.
    document.addEventListener('resign',
        () => {
            this.log.info(`OpenPOS received app 'resign' notification`);
            this.onAppEnteringBackground.next(true);
        },
        false
    );
    document.addEventListener('resume',
        () => {
            this.log.info(`OpenPOS received app 'resume' notification`);
            this.onAppEnteringForeground.next(true);
        },
        false
    );
    document.addEventListener('active',
        () => {
            this.log.info(`OpenPOS received app 'active' notification`);
            this.onAppEnteredForeground.next(true);
        },
        false
    );

    this.session.registerMessageHandler(this, 'DeviceRequest');

  }

  public async isInAppBrowserActive() {
    try {
        const inAppPlugin = <InAppBrowserPlugin> await this.pluginService.getPlugin('InAppBrowser');
        return inAppPlugin.isActive();
    } catch (error) {
        this.log.info(`InAppBrowser plugin not available. Reason: ${error}`);
        return false;
    }
  }

  public isCameraScanInProgress() {
      this.log.info(`isCameraScanInProgress? ${this.cameraScanInProgress}`);
      return this.cameraScanInProgress;
  }

  handle(message: any) {
      this.onDeviceRequest(message);
  }

  protected initializeBarcodeScannerPlugin(): void {
    // Add barcdode scanner plugin as a device plugin so it can also be invoked
    // from the server-side in addition to from within the DeviceService
    const barcodeScannerPlugin = new BarcodeScannerPlugin();
    this.pluginService.addPlugin(barcodeScannerPlugin.pluginName, barcodeScannerPlugin);
  }

  protected initializeInAppBrowserPlugin(): void {
      const inAppBrowserPlugin = new InAppBrowserPlugin();
      this.pluginService.addPlugin(inAppBrowserPlugin.pluginId, inAppBrowserPlugin);
      this.log.info('InAppBrowserPlugin initialized.');
  }

  protected initializeLogfileDownloadPlugin(): void {
      const logfileDownloadPlugin = new LogfileDownloadPlugin(this.fileUploadService);
      this.pluginService.addPlugin(logfileDownloadPlugin.pluginId, logfileDownloadPlugin);
      this.log.info('LogfileDownloadPlugin initialized.');
  }

  public scan(source?: string) {
    if (this.screen.template && this.screen.template.scan &&
        this.screen.template.scan.scanType === 'CAMERA_CORDOVA') {
        this.log.info(`request to scan was made for: ${this.screen.template.scan.scanType}`);
        this.cordovaCameraScan(source);
    } else {
        this.log.info(`FAILED to invoke scan. Is there a screen.template.scan.scanType?`) ;
    }
  }

  public cordovaCameraScan(source?: string) {
    if (!this.session.isRunningInBrowser() && cordova) {
      this.pluginService.getDevicePlugin('barcodeScannerPlugin').then(plugin => {
        this.cameraScanInProgress = true;
        plugin.processRequest(
          {requestId: 'scan', deviceId: 'barcode-scanner', type: null, subType: null, payload: null},
          (response) => {
            if (response && ! response.cancelled) {
              if (response instanceof Scan && source && ! response.source) {
                  response.source = source;
              }
              this.session.response = response;
              this.session.onAction('Scan');
            }
            this.cameraScanInProgress = false;
        },
          (error) => {
            this.cameraScanInProgress = false;
            console.error('Scanning failed: ' + error);
          }
        );
      }).catch(error => {
        this.cameraScanInProgress = false;
        console.error(`barcodeScannerPlugin error: ${error}`);
      });
    }
  }

  public onDeviceRequest = (request: IDeviceRequest) => {
    this.log.info(`request received for device: ${request.deviceId}`);
    // targetted plugin is assumed to be a cordova plugin

    const pluginLookupKey = request.pluginId ? request.pluginId : request.deviceId;
    const targetPluginPromise: Promise<IDevicePlugin> = this.pluginService.getDevicePlugin(pluginLookupKey);

    targetPluginPromise.then( (targetPlugin: IDevicePlugin) => {
        this.log.info(`targetPlugin = pluginId: ${targetPlugin.pluginId}, pluginName: ${targetPlugin.pluginName}`);
        this.log.info(`Sending request '${request.subType}:${request.requestId}' to device/plugin '${pluginLookupKey}'...`);
        targetPlugin.processRequest(
          request,
          (response) => {
            this.session.onDeviceResponse( {
                requestId: request.requestId,
                deviceId: request.deviceId,
                type: DEVICE_RESPONSE_TYPE,
                payload: response
              }
            );
          },
          (error) => {
              this.session.onDeviceResponse( {
                  requestId: request.requestId,
                  deviceId: request.deviceId,
                  type: DEVICE_ERROR_RESPONSE_TYPE,
                  payload: error
                }
              );
          }
        );
      }
    ).catch( (error) => {
        const msg = 'No handling yet (or plugin may not be initialized) for ' +
          `device/plugin with key: ${pluginLookupKey}. request '${request.subType}:${request.requestId}' will be ignored. Error: ${error}`;

        this.session.onDeviceResponse( {
            requestId: request.requestId,
            deviceId: request.deviceId,
            type: DEVICE_DNE_RESPONSE_TYPE,
            payload: msg
          }
        );
        this.log.info(msg);
      }
    );
  }

  public isRunningInCordova(): boolean {
    if (this._isRunningInCordova == null) {
      this._isRunningInCordova = typeof cordova !== 'undefined' && ! this.session.isRunningInBrowser();
    }

    return this._isRunningInCordova;
  }

  public isRunningInBrowser(): boolean {
    return this.session.isRunningInBrowser();
  }


    private handleBackgrounding() {
        /* 10/12/18 -- Commented out showing of splashscreen due to improved show/cancel loading
           that we added.  Will remove once fully vetted in QA.
        // Show splash screen when running on iOS
        // Ascena has a Startup task that automatically dismisses the splash screen.
        // If other retailers are using iOS will need to implement the same, move this code
        // to Ascena, or find the right place in the core to dismiss the splash screen

        if ((<any>(window.navigator)).splashscreen) {
            this.log.info('Showing splashscreen');
            (<any>(window.navigator)).splashscreen.show();
        }
        */
        this.session.cancelLoading();
        // Input will get unblocked once re-subscribed to server and current screen is shown
        this.log.info('Entering into background');
        this.session.inBackground = true;
    }

    private handleForegrounding() {
        // check for any changes while were are inactive
        // We'll reset the inBackground flag after we receive the response
        this.session.publish('Refresh', 'Screen');
        this.log.info('Start coming back into foreground. Screen refresh requested.');
        /* 10/12/18 -- Commented out hiding of splashscreen due to improved show/cancel loading
           that we added.  Will remove once fully vetted in QA.
        if ((<any>(window.navigator)).splashscreen) {
            this.log.debug('Hiding splashscreen');
            (<any>(window.navigator)).splashscreen.hide();
        }
        */
    }

}
