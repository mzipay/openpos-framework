import { Injectable } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Observable, Subject } from 'rxjs';
import { filter } from 'rxjs/operators';
import { SessionService } from '../../../services/session.service';
import {OpenposScanType} from '../openpos-scan-type.enum';
import { IScanData } from '../scan.interface';
import { IScanner } from '../scanner.interface';
import { ScanditScannerViewComponent } from './scandit-scanner-view/scandit-scanner-view.component';
import { ScanditBarcodeUtils } from '../scandit-scanner-cordova/scandit-to-openpos-barcode-type';
import { configure, BarcodePicker, Barcode, ScanSettings } from "scandit-sdk";

@Injectable()
export class ScanditScannerWebPlugin implements IScanner {

    private scanData$ = new Subject<IScanData>();
    private dialogRef: MatDialogRef<ScanditScannerViewComponent>;
    private config: any;

    constructor(sessionService: SessionService, private matDialog: MatDialog) {

        sessionService.getMessages('ConfigChanged').pipe(
            filter(m => m.configType === 'ScanditWebPlugin')
        ).subscribe(m => {
            this.config = m;
            configure(m.licenseKey, {
                engineLocation: m.engineLocation
            }).then((res) => {
                console.log(res)
            })
        });

        sessionService.getMessages('Screen', 'Dialog').subscribe(m => {
            if (this.dialogRef) {
                this.dialogRef.close();
            }
        });
    }

    startScanning(): Observable<IScanData> {
        return this.scanData$;
    }

    stopScanning() {
    }

    triggerScan() {
        this.dialogRef = this.matDialog.open(ScanditScannerViewComponent);
        this.dialogRef.afterOpened().subscribe(() => {
            BarcodePicker.create(document.getElementById("scandit-barcode-picker"), {
                playSoundOnScan: true,
                vibrateOnScan: true
            }).then((barcodePicker) => {
                const scanSettings = new ScanSettings({
                    // here we should set symobologies that we want to use in scanner
                    enabledSymbologies: [
                        Barcode.Symbology.AZTEC,
                        Barcode.Symbology.CODABAR,
                        Barcode.Symbology.CODE11,
                        Barcode.Symbology.CODE25,
                        Barcode.Symbology.CODE39,
                        Barcode.Symbology.CODE93,
                        Barcode.Symbology.CODE128,
                        Barcode.Symbology.EAN13,
                        Barcode.Symbology.EAN8,
                        Barcode.Symbology.GS1_DATABAR,
                        Barcode.Symbology.MAXICODE,
                        Barcode.Symbology.MSI_PLESSEY,
                        Barcode.Symbology.PDF417,
                        Barcode.Symbology.QR,
                        Barcode.Symbology.RM4SCC,
                        Barcode.Symbology.EAN13,
                        Barcode.Symbology.UPCE,
                        Barcode.Symbology.INTERLEAVED_2_OF_5
                    ],
                    codeDuplicateFilter: 1000
                });
                barcodePicker.applyScanSettings(scanSettings);
                barcodePicker.addListener("scan", (scanResult)=>{ this.processScan(scanResult,this.scanData$) })
            });
            }
        )
    }

    private processScan(scanResult: any, scanData$: Subject<IScanData>) {

        if(scanResult.barcodes.length){
            const scanData = (
                {
                    type: ScanditBarcodeUtils.convertToOpenposType(scanResult.barcodes[0].symbology, scanResult.barcodes[0].data),
                    data: scanResult.barcodes[0].data
                }
            ) as IScanData;

            // Scandit adds a leading 0 to UPCA scans and we need to strip it off
            if(scanData.type === OpenposScanType.UPCA){
                scanData.data = scanData.data.substring(1);
            }

            scanData$.next(scanData);
        }
    }
}