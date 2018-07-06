import { Subscription } from 'rxjs';
import { Directive, ElementRef, OnDestroy, OnInit } from '@angular/core';
import { BarcodeScannerPlugin } from '../../plugins/barcodescanner.plugin';
import { SessionService, DeviceService, PluginService } from '../../core';
import { Scan } from '../../common/scan';

@Directive({
    selector: '[barcodeScanPublisher]'
})
export class BarcodeScanPublisherDirective implements OnInit, OnDestroy {

    private barcodePlugin: BarcodeScannerPlugin;
    private barcodeEventSubscription: Subscription;
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
                this.barcodeEventSubscription = this.barcodePlugin.onBarcodeScanned.subscribe({
                    next: (scan: Scan) => {
                        this.publishBarcode(scan);
                    }
                });
            }).catch( error => console.log(`Failed to get barcodeScannerPlugin.  Error: ${error}`) );
         });
    }    

    ngOnDestroy(): void {
        if (this.barcodeEventSubscription) {
            this.barcodeEventSubscription.unsubscribe();
        }
    }

    publishBarcode(scan: Scan) {
        if (! this.sessionService.dialog) {
            // Publish barcode to the server
            console.log(`Got barcode scan, publishing '${scan.value}'...`);
            this.sessionService.response = scan;
            this.sessionService.onAction('Scan');
        } else {
            console.log(`Not publishing barcode '${scan.value}' because a dialog is showing`);
        }
    }
}
