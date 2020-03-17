import {Injectable} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {Observable, Subject} from 'rxjs';
import {filter, takeUntil} from 'rxjs/operators';
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
    private settings = new Scandit.BarcodeCaptureSettings();
    private barcodeCapture: any = {};
    private cameraSettings;
    private context: any;
    private dialogRef: MatDialogRef<ScanditCameraViewComponent>;
    private listener: any;


    constructor( sessionService: SessionService, private matDialog: MatDialog) {


        sessionService.getMessages('ConfigChanged').pipe(
            filter( m => m.configType === 'ScanditCordova')
        ).subscribe( m => {
            if(m.licenseKey){
                this.licenseKey = m.licenseKey;
            }
            Object.getOwnPropertyNames(m).forEach( propName => {
                if(this.settings.hasOwnProperty(propName)){
                    this.settings[propName] = m[propName];
                }
            });
            if(m.enabledCodes){
                let codes = m.enabledCodes.split(',');
                codes.forEach( code => this.settings.enableSymbology(ScanditBarcodeUtils.convertFromOpenposType(code.trim()), true));
            }
        });

        sessionService.getMessages('Screen', 'Dialog').subscribe( m => {
            if(this.dialogRef){
                this.dialogRef.close();
            }
        });
    }


    initialize(): Observable<string> {
        return Observable.create( (initialized: Subject<string>) => {
            this.context = Scandit.DataCaptureContext.forLicenseKey(this.licenseKey);
            this.barcodeCapture = Scandit.BarcodeCapture.forContext(this.context, this.settings);
            this.barcodeCapture.isEnabled = false;
            const camera = Scandit.Camera.default;

            if(!this.cameraSettings){
                this.cameraSettings = Scandit.BarcodeCapture.recommendedCameraSettings;
            }

            if (camera) {
                camera.applySettings(this.cameraSettings);
            }
            this.context.setFrameSource(camera);
            camera.switchToDesiredState(Scandit.FrameSourceState.On);
            this.listener = {
                didScan: (barcodeCapture, session) => {
                    barcodeCapture.isEnabled = false;
                    if(this.dialogRef){
                        this.dialogRef.close();
                    }
                    setTimeout(() =>this.processScan(session));
                }
            };
            this.barcodeCapture.addListener(this.listener);
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
        return this.scanData$;
    }

    stopScanning() {
    }

    triggerScan() {
        this.barcodeCapture.isEnabled = true;
        this.dialogRef = this.matDialog.open(ScanditCameraViewComponent);
        this.dialogRef.afterOpened().subscribe( () =>{
                const view = Scandit.DataCaptureView.forContext(this.context);
                view.connectToElement(document.getElementById("scandit-capture-view"));
                const overlay = Scandit.BarcodeCaptureOverlay.withBarcodeCaptureForView(this.barcodeCapture, view);
                const viewfinder = new Scandit.LaserlineViewfinder();
                overlay.viewfinder = viewfinder;
            }
        )
    }

    private processScan( session: any){
        console.log("Scanned ", session);
        let scanData = ({type: ScanditBarcodeUtils.convertToOpenposType(session.newlyRecognizedBarcodes[0].symbology), data: session.newlyRecognizedBarcodes[0].data}) as IScanData;
        this.scanData$.next(scanData);
    }
}