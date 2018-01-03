import { IDevicePlugin } from './../common/idevice-plugin';
import { PluginService } from './plugin.service';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subject } from 'rxjs/Subject';
import { IDeviceRequest } from './../common/idevicerequest';
import { SessionService } from './session.service';
import { Injectable } from '@angular/core';
import { Scan } from '../common/scan';

declare var cordova: any;

@Injectable()
export class DeviceService {
  public onDeviceReady: Subject<string> = new BehaviorSubject<string>(null);

  constructor(protected session: SessionService, public pluginService: PluginService) {
    document.addEventListener('deviceready', () => {
      console.log('cordova devices are ready');
      this.onDeviceReady.next(`Application is initialized on platform '${cordova.platform}'`);
    },
    false);

    // Listen for requests made from the server targeted to a specific device
    this.session.onDeviceRequest.subscribe({
      next: (event: IDeviceRequest) => {
        this.onDeviceRequest(event);
      }
    });
  }

  public scan() {
    console.log('request to scan was made for: ' + this.session.screen.scanType);
    if (this.session.screen.scanType && this.session.screen.scanType === 'CAMERA_CORDOVA') {
      this.cordovaCameraScan();
    }
  }

  public cordovaCameraScan() {
    if (!this.session.isRunningInBrowser() && cordova) {
      console.log('attempting to enable camera scanner');
      const self = this;
      cordova.plugins.barcodeScanner.scan(
        function (result) {
          if (!result.cancelled) {
            self.session.response = new Scan(result.text, result.format);
            self.session.onAction('Scan');
          }
          console.log('We got a barcode\n' +
            'Result: ' + result.text + '\n' +
            'Format: ' + result.format + '\n' +
            'Cancelled: ' + result.cancelled);
        },
        function (error) {
          console.error('Scanning failed: ' + error);
        },
        {
          preferFrontCamera: false, // iOS and Android
          showFlipCameraButton: false, // iOS and Android
          showTorchButton: false, // iOS and Android
          torchOn: false, // Android, launch with the torch switched on (if available)
          saveHistory: false, // Android, save scan history (default false)
          prompt: 'Place a barcode inside the scan area', // Android
          resultDisplayDuration: 500, // Android, display scanned text for X ms. 0 suppresses it entirely, default 1500
          formats: 'CODE_128,EAN_8,EAN_13,UPC_A,UPC_E', // default: all but PDF_417 and RSS_EXPANDED
          orientation: 'landscape', // Android only (portrait|landscape), default unset so it rotates with the device
          disableAnimations: false, // iOS
          disableSuccessBeep: false // iOS and Android
        }
      );
    }
  }

  public onDeviceRequest = (request: IDeviceRequest) => {
    console.log(`request received for device: ${request.deviceId}`);
    // targetted plugin is assumed to be a cordova plugin

    const pluginLookupKey = request.pluginId ? request.pluginId : request.deviceId;
    const targetPluginPromise: Promise<IDevicePlugin> = this.pluginService.getDevicePlugin(pluginLookupKey);

    targetPluginPromise.then( (targetPlugin: IDevicePlugin) => {
        console.log(`targetPlugin = pluginId: ${targetPlugin.pluginId}, pluginName: ${targetPlugin.pluginName}`);
        console.log(`Sending request '${request.subType}:${request.requestId}' to device/plugin '${pluginLookupKey}'...`);
        targetPlugin.processRequest(
          () => request.payload,
          (response) => {
            this.session.onDeviceResponse( {
                requestId: request.requestId,
                deviceId: request.deviceId,
                type: 'DeviceResponse',
                payload: response
              }
            );
          },
          (error) => {
              this.session.onDeviceResponse( {
                  requestId: request.requestId,
                  deviceId: request.deviceId,
                  type: 'DeviceErrorResponse',
                  payload: error
                }
              );
          }
        );
      }
    ).catch( (error) => {
      console.warn( 'No handling yet (or plugin may not be initialized) for ' +
        `device/plugin with key: ${pluginLookupKey}. request '${request.subType}:${request.requestId}' will be ignored.`);
      }
    );
  }

}
