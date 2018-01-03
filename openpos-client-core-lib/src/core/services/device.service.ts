import { IDeviceRequest } from './../common/idevicerequest';
import { SessionService } from './session.service';
import { IMenuItem } from '../common/imenuitem';
import { LoaderService } from '../common/loader/loader.service';
import { IDialog } from '../common/idialog';
import { Observable } from 'rxjs/Observable';
import { Message } from '@stomp/stompjs';
import { Subscription } from 'rxjs/Subscription';
import { Injectable } from '@angular/core';
import { StompService, StompState } from '@stomp/ng2-stompjs';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { Scan } from '../common/scan';

declare var cordova: any;

@Injectable()
export class DeviceService {

  constructor(protected session: SessionService) {
    document.addEventListener('deviceready', function () {
      console.log('cordova devices are ready');
    }, false);

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
        function (result: any) {
          if (!result.cancelled) {
            self.session.response = new Scan(result.text, result.format);
            self.session.onAction('Scan');
          }
          console.log('We got a barcode\n' +
            'Result: ' + result.text + '\n' +
            'Format: ' + result.format + '\n' +
            'Cancelled: ' + result.cancelled);
        },
        function (error: any) {
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
    console.log(`deviceRequest received: ${request.payload}`);
    // TODO: For now just hardcoding a response
    this.session.onDeviceResponse({
      requestId: request.requestId,
      deviceId: request.deviceId,
      type: 'DeviceResponse',
      payload: `Successfully processed request ${request.requestId}`
    });
  }


}


