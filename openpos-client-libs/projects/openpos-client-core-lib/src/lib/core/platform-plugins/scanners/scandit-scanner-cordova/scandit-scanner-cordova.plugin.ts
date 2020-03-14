import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {Observable, Subject} from 'rxjs';
import {filter, takeUntil} from 'rxjs/operators';
import {hasOwnProperty} from 'tslint/lib/utils';
import {SessionService} from '../../../services/session.service';
import {IPlatformPlugin} from '../../platform-plugin.interface';
import {IScanData} from '../scan.interface';
import {IScanner} from '../scanner.interface';
import {ScanditCameraViewComponent} from './scandit-camera-view/scandit-camera-view.component';
import {ScanditBarcodeUtils} from './scandit-to-openpos-barcode-type';

declare var Scandit: any;

@Injectable()
export class ScanditScannerCordovaPlugin implements IScanner, IPlatformPlugin {

    private licenseKey = "";
    private scanData$ = new Subject<IScanData>();
    private stopScanning$ = new Subject();
    private settings = new Scandit.BarcodeCaptureSettings();
    private barcodeCapture: any = {};
    private cameraSettings = Scandit.BarcodeCapture.recommendedCameraSettings;
    private context: any;


    constructor( sessionService: SessionService, private matDialog: MatDialog) {
        sessionService.getMessages('ConfigChanged').pipe(
            filter( m => m.configType === 'ScanditCordova')
        ).subscribe( m => {
            if(m.licenseKey){
                this.licenseKey = m.licenseKey;
            }
            Object.getOwnPropertyNames(m).forEach( propName => {
                if(hasOwnProperty(this.settings, propName)){
                    this.settings[propName] = m[propName];
                }
            });
            if(m.enabledCodes){
                let codes = m.enabledCodes.split(',');
                codes.forEach( code => this.settings.enableSymbology(ScanditBarcodeUtils.convertFromOpenposType(code.trim()), true));
            }
        });
    }


    initialize(): Observable<string> {
        return Observable.create( (initialized: Subject<string>) => {
            this.context = Scandit.DataCaptureContext.contextForLicenseKey(this.licenseKey);
            this.barcodeCapture = Scandit.BarcodeCapture.forContext(this.context, this.settings);
            const camera = Scandit.Camera.default;

            if (camera) {
                camera.applySettings(this.cameraSettings);
            }
            this.context.setFrameSource(camera);
            camera.switchToDesiredState(Scandit.FrameSourceState.On);
            initialized.complete();
        });
    }

    name(): string {
        return 'ScanditCordova';
    }

    pluginPresent(): boolean {
        return window['Scandit'];
    }

    startScanning(): Observable<IScanData> {
        const listener = {
            didScan: (barcodeCapture, session) => {
                this.processScan(session);
            }
        };
        this.barcodeCapture.addListener(listener);

        return this.scanData$.pipe(takeUntil(this.stopScanning$))
    }

    stopScanning() {
        this.stopScanning$.next();
    }

    triggerScan() {
        this.matDialog.open(ScanditCameraViewComponent);
        const view = Scandit.DataCaptureView.viewForContext(this.context);
        view.connectToElement(document.getElementById("scandit-capture-view"));
        const overlay = new Scandit.BarcodeCaptureOverlay(this.barcodeCapture, view);
    }

    private processScan( session: any){
        console.log("Scanned " + session.newlyRecognizedCodes[0].symbology + " code: " + session.newlyRecognizedCodes[0].data);
        let scanData = ({type: ScanditBarcodeUtils.convertToOpenposType(session.newlyRecognizedCodes[0].symbology), data: session.newlyRecognizedCodes[0].data}) as IScanData;
        this.scanData$.next(scanData);
        session.stopScanning();
    }
}