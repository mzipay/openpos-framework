import { Scan } from './scan';
import { BarcodeScannerPlugin } from './../plugins/barcodescanner.plugin';
import { PluginService } from './../services/plugin.service';
import { SessionService } from './../services/session.service';
import { Directive, Input, ElementRef, AfterContentInit, OnDestroy, OnInit } from '@angular/core';
import { DeviceService } from '../services/device.service';

@Directive({
    selector: '[barcodeScanPublisher]'
})
export class BarcodeScanPublisherDirective implements OnInit, OnDestroy {

    private barcodePlugin: BarcodeScannerPlugin;
    constructor(el: ElementRef, 
        private sessionService: SessionService,
        private deviceService: DeviceService, 
        private pluginService: PluginService) {
    }


    ngOnInit(): void {
        this.deviceService.onDeviceReady.subscribe(message => {
            this.pluginService.getDevicePlugin('barcodeScannerPlugin').then(plugin => {
                console.log('BarcodeScanPublisherDirective INITTED, got barcodeScannerPlugin');
                this.barcodePlugin = <BarcodeScannerPlugin> plugin;
                // the onBarcodeScanned will only emit an event when client code passes a scan
                // event to the plugin.  This won't be called for cordova barcodescanner plugin
                // camera-based scan events.  It should only be used for third party scan events
                // which come from other sources such as a scan device
                this.barcodePlugin.onBarcodeScanned.subscribe({
                    next: (scan: Scan) => {
                        this.publishBarcode(scan);
                    }
                });
            }).catch( error => console.log(`Failed to get barcodeScannerPlugin.  Error: ${error}`) );
         });
    }    

    ngOnDestroy(): void {
//        console.log('BarcodeScanPublisherDirective DESTROYED');
    }

    publishBarcode(scan: Scan) {
        // Publish barcode to the server
        console.log(`Got barcode scan, publishing '${scan.value}'...`);
        this.sessionService.response = scan;
        this.sessionService.onAction('Scan');
    }
}
