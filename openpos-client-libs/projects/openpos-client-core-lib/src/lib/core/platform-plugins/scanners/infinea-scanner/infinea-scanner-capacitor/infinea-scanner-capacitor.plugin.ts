import {Injectable} from '@angular/core';
import {IPlatformPlugin} from '../../../platform-plugin.interface';
import {IScanner} from '../../scanner.interface';
import {Observable, of, Subject, throwError} from 'rxjs';
import {IScanData} from '../../scan.interface';
import {Capacitor, PluginListenerHandle, Plugins as CapacitorPlugins} from "@capacitor/core";
import {ConfigurationService} from "../../../../services/configuration.service";
import {map, switchMap, take, timeout} from "rxjs/operators";
import {ConfigChangedMessage} from "../../../../messages/config-changed-message";
import {InfineaBarcodeUtils} from "../infinea-to-openpos-barcode-type";

declare module '@capacitor/core' {
    interface PluginRegistry {
        InfineaScannerCapacitor: InfineaPlugin;
    }
}

export interface InfineaPlugin {
    addListener(event: 'scan', callback: (e) => void): PluginListenerHandle;
}

@Injectable({
    providedIn: 'root'
})
export class InfineaScannerCapacitorPlugin implements IPlatformPlugin, IScanner {
    constructor(private _config: ConfigurationService) {
    }

    private scanData$ = new Subject<IScanData>();

    name(): string {
        return 'InfineaScannerCapacitor';
    }

    pluginPresent(): boolean {
        return Capacitor.isNative && Capacitor.isPluginAvailable('InfineaScannerCapacitor');
    }

    initialize(): Observable<string> {

        // return of(CapacitorPlugins.InfineaScannerCapacitor.initialize({
        //     apiKey: 'UtJtVhO9yImiQhADyh+0PqEQG0eotVpTsBN9IALb0maa2pUMXH1GvIlhzNLEsIrmFoGs4rumE1Ex4nFR9sHWy1Liox7o/7nvGYfz/kOrisY='
        // })).pipe(
        //     map(() => "initialized Infinea Scanner for Capacitor")
        // );
        return this._config.getConfiguration('InfineaScannerCapacitor').pipe(
            take(1),
            timeout(10000),
            switchMap((config: ConfigChangedMessage & any) => {
                if (config.licenseKey) {
                    return of(CapacitorPlugins.InfineaCapacitor.initialize({
                        apiKey: config.licenseKey
                    }));
                }

                return throwError('could not find Infinea license key');
            }),
            map(() => "initialized Infinea Scanner for Capacitor")
        );
    }

    startScanning(): Observable<IScanData> {
        return new Observable(observer => {
            const handle = CapacitorPlugins.InfineaScannerCapacitor.addListener('scan', (e) => {
                observer.next({
                    type: InfineaBarcodeUtils.convertToOpenposType(e.type),
                    data: e.barcode
                });
            });

            return () => {
                handle.remove();
                CapacitorPlugins.InfineaScannerCapacitor.stopScan();
            };
        });
    }

    stopScanning() {
        /*this.infineaPlugin.barcodeScan(() => {
            console.log(`Time| ${new Date()} || Disable Barcode Scan| Success`);
        }, () => {
            console.log(`Time| ${new Date()} || Disable Barcode Scan| Failure`);
        }, 'false');*/
    }

    triggerScan() {
    }
}
