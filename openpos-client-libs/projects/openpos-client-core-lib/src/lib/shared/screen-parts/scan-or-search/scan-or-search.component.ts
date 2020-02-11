import { IActionItem } from '../../../core/actions/action-item.interface';
import {
    Component,
    Input,
    Output,
    EventEmitter,
    OnInit,
    OnDestroy,
    Injector,
    ElementRef,
    Renderer2, ViewChild, HostListener
} from '@angular/core';
import { ScreenPartComponent } from '../screen-part';
import { ScanOrSearchInterface } from './scan-or-search.interface';
import { DeviceService } from '../../../core/services/device.service';
import { ScreenPart } from '../../decorators/screen-part.decorator';
import { OpenposMediaService, MediaBreakpoints } from '../../../core/media/openpos-media.service';
import { Observable, Subscription } from 'rxjs';
import { ScannerService } from '../../../core/platform-plugins/scanners/scanner.service';
import { OnBecomingActive } from '../../../core/life-cycle-interfaces/becoming-active.interface';
import { OnLeavingActive } from '../../../core/life-cycle-interfaces/leaving-active.interface';
import { KeyPressProvider} from "../../providers/keypress.provider";
import {SessionService} from "../../../core/services/session.service";
import {ScanOrSearchProvider} from "./scan-or-search.service";

@ScreenPart({
    name: 'scanOrSearch'
})
@Component({
    selector: 'app-scan-or-search',
    templateUrl: './scan-or-search.component.html',
    styleUrls: ['./scan-or-search.component.scss']
})
export class ScanOrSearchComponent extends ScreenPartComponent<ScanOrSearchInterface> implements
    OnInit, OnDestroy, OnBecomingActive, OnLeavingActive {

    public barcode: string;
    isMobile$: Observable<boolean>;

    @Input() defaultAction: IActionItem;

    @Input() focusInitial = true;

    @Output() change: EventEmitter<string> = new EventEmitter<string>();

    @ViewChild('input', { read: ElementRef }) private input: ElementRef;

    keyboardLayout = 'US Standard';

    private autoFocusPattern: RegExp;

    private scanServiceSubscription: Subscription;

    constructor( sessionService: SessionService, public devices: DeviceService, injector: Injector, private el: ElementRef, private renderer: Renderer2,
                mediaService: OpenposMediaService, private scannerService: ScannerService, protected keyPresses: KeyPressProvider, protected scanOrSearchProvider: ScanOrSearchProvider) {
        super(injector);
        const mobileMap = new Map([
            [MediaBreakpoints.MOBILE_PORTRAIT, true],
            [MediaBreakpoints.MOBILE_LANDSCAPE, true],
            [MediaBreakpoints.TABLET_PORTRAIT, false],
            [MediaBreakpoints.TABLET_LANDSCAPE, false],
            [MediaBreakpoints.DESKTOP_PORTRAIT, false],
            [MediaBreakpoints.DESKTOP_LANDSCAPE, false]
        ]);
        this.isMobile$ = mediaService.observe(mobileMap);
        this.autoFocusPattern = new RegExp(scanOrSearchProvider.getKeyPressAutofocusPattern());
    }

    ngOnInit(): void {
        super.ngOnInit();
        this.registerScanner();
        if (this.focusInitial) {
            this.renderer.addClass( this.el.nativeElement, 'focusInitial');
        } else {
            this.renderer.removeClass(this.el.nativeElement, 'focusInitial');
        }
        if (this.screenData.keyboardLayout) {
            this.keyboardLayout = this.screenData.keyboardLayout;
        }
    }

    @HostListener('document:keydown', ['$event'])
    onKeyDown(event: KeyboardEvent){
        if (event.key.match(this.autoFocusPattern)){
            if (this.input.nativeElement !== document.activeElement) {
                this.input.nativeElement.value += event.key;
            }
            this.subscriptions.add(this.keyPresses.subscribe(event.key, 1, () => {
                this.input.nativeElement.focus();
            }));
        }
    }

    onBecomingActive() {
        this.registerScanner();
    }

    onLeavingActive() {
        this.unregisterScanner();
    }

    ngOnDestroy(): void {
        this.unregisterScanner();
        // this.scannerService.stopScanning();
        super.ngOnDestroy();
    }

    private registerScanner() {
        if (typeof this.scanServiceSubscription === 'undefined' || this.scanServiceSubscription === null) {
            this.scanServiceSubscription = this.scannerService.startScanning().subscribe(scanData => {
                this.doAction(this.screenData.scanAction, scanData);
            });
        }
    }

    private unregisterScanner() {
        if (this.scanServiceSubscription !== null) {
            this.scanServiceSubscription.unsubscribe();
            this.scanServiceSubscription = null;
        }
    }

    screenDataUpdated() {
    }

    public onEnter($event: any): void {
        if (this.barcode && this.barcode.trim().length >= this.screenData.scanMinLength) {
            $event.stopImmediatePropagation();
            this.doAction(this.screenData.keyedAction, this.barcode);
            this.barcode = '';
        } else if (this.defaultAction && this.defaultAction.enabled) {
            $event.stopImmediatePropagation();
            this.doAction(this.defaultAction);
        }
    }

    public onValueChange($event: any): void {
        this.change.emit(this.barcode);
    }
}
