import { Injectable } from '@angular/core';
import { IPlatformPlugin } from './platform-plugin.interface';
import { IScanner } from './scanner.interface';
import { Observable, Subject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class InfineaScannerCordovaPlugin implements IPlatformPlugin, IScanner {

    private infineaPlugin;
    private scanData$ = new Subject<string>();

    name(): string {
        return 'InfineaScanner';
    }

    pluginPresent(): boolean {
        return !!window.hasOwnProperty('Infinea');
    }

    initialize(): Observable<string> {
        return Observable.create( (initialized: Subject<string>) => {
            // tslint:disable-next-line:no-string-literal
            this.infineaPlugin = window['Infinea'];

            this.infineaPlugin.barcodeDataCallback = (barcode, type, typeText) => {
                this.scanData$.next(this.parseBarcode(barcode, type, typeText));
            };

            initialized.next('Initializing Infinea Plugin');

            this.infineaPlugin.init(() => {
                initialized.next(`Infinea Plugin Initialized Success`);
                initialized.complete();
            }, () => {
                initialized.error(`Infinea Plugin Initialized Failure`);
            });
        });
    }

    startScanning(): Observable<string> {
        this.infineaPlugin.barcodeScan(() => {
            console.log(`Infinea Enable Barcode Scan Success`);
        }, () => {
            console.log(`Infinea Enable Barcode Scan Failure`);
        }, 'true');

        return this.scanData$;
    }

    stopScanning() {
        this.infineaPlugin.barcodeScan(() => {
            console.log(`Time| ${new Date()} || Disable Barcode Scan| Success`);
        }, () => {
            console.log(`Time| ${new Date()} || Disable Barcode Scan| Failure`);
        }, 'false');
    }

    private parseSKUFromBarcode(barcode: string): string {
        return barcode.substring(3, 12);
    }

    private parseUPCEANFromBarcode(barcode: string): string {
        if (barcode[0] === '0') {
            return barcode.slice(1, barcode.length);
        }
        return barcode;
    }

    private parseBarcode(barcode: string, type: string, text: string) {
        if (text.toLowerCase().includes('code 128') && barcode.length === 20) {
            return this.parseSKUFromBarcode(barcode);
        } else if (text.toLowerCase().includes('upc') || text.toLowerCase().includes('ean')) {
            return this.parseUPCEANFromBarcode(barcode);
        } else {
            return barcode;
        }
    }
  }
