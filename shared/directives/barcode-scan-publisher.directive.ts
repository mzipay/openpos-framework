import { Logger } from './../../core/services/logger.service';
import { Subscription } from 'rxjs';
import { Directive, ElementRef, OnDestroy, OnInit } from '@angular/core';
import { SessionService, DeviceService, PluginService, Scan, BarcodeScannerPlugin, DialogService } from '../../core';

@Directive({
    // tslint:disable-next-line:directive-selector
    selector: '[barcodeScanPublisher]'
})
export class BarcodeScanPublisherDirective implements OnInit, OnDestroy {

    private barcodePlugin: BarcodeScannerPlugin;
    private barcodeEventSubscription: Subscription;
    constructor(el: ElementRef,
        private log: Logger,
        private sessionService: SessionService,
        private deviceService: DeviceService,
        private dialogService: DialogService,
        private pluginService: PluginService) {
    }


    ngOnInit(): void {
        this.deviceService.onDeviceReady.subscribe(message => {
            if (message) {
                this.pluginService.getDevicePlugin('barcodeScannerPlugin').then(plugin => {
                    this.log.info('BarcodeScanPublisherDirective INITTED, got barcodeScannerPlugin');
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
                }).catch( error => this.log.info(`Failed to get barcodeScannerPlugin.  Error: ${error}`) );
            }
         });
    }

    ngOnDestroy(): void {
        if (this.barcodeEventSubscription) {
            this.barcodeEventSubscription.unsubscribe();
        }
    }

    publishBarcode(scan: Scan) {
        if (! this.dialogService.isDialogOpen()) {
            // Publish barcode to the server
            this.log.info(`Got barcode scan, publishing '${scan.value}'...`);
            this.sessionService.response = scan;
            this.sessionService.onAction('Scan');
        } else {
            this.log.info(`Not publishing barcode '${scan.value}' because a dialog is showing`);
        }
    }
}
