import { FocusService } from './../../../core/services/focus.service';
import { IActionItem } from './../../../core/interfaces/menu-item.interface';
import { Component, ViewChild, AfterViewInit, Input, ElementRef } from '@angular/core';
import { ScreenPartComponent } from '../screen-part';
import { ScanOrSearchInterface } from './scan-or-search.interface';
import { MatInput } from '@angular/material';
import { DeviceService } from '../../../core/services/device.service';
import { MessageProvider } from '../../providers/message.provider';
import { ScreenPart } from '../../decorators/screen-part.decorator';

@ScreenPart({
    name: 'scan'
})
@Component({
    selector: 'app-scan-or-search',
    templateUrl: './scan-or-search.component.html',
    styleUrls: ['./scan-or-search.component.scss']
})
export class ScanOrSearchComponent extends ScreenPartComponent<ScanOrSearchInterface> implements AfterViewInit {

    public barcode: string;

    @Input() defaultAction: IActionItem;

    constructor( public devices: DeviceService, messageProvider: MessageProvider,
                 private focusService: FocusService, private elRef: ElementRef) {
        super(messageProvider);
    }

    screenDataUpdated() {
        this.focusFirst();
    }

    ngAfterViewInit(): void {
        this.focusFirst();
    }

    public onEnter(): void {
        if (this.barcode && this.barcode.trim().length >= this.screenData.scanMinLength) {
            this.sessionService.onAction(this.screenData.scanActionName, this.barcode);
            this.barcode = '';
        } else if (this.defaultAction && this.defaultAction.enabled) {
            this.sessionService.onAction(this.defaultAction);
        }
    }

    protected setDefaultFocus() {

    }

    private focusFirst(): void {
        if (this.screenData && this.screenData.autoFocusOnScan) {
            const parent = this.elRef.nativeElement.parentNode;
            const input = parent.getElementsByTagName('input')[0];
            if (input) {
                this.focusService.requestFocus('scan-or-search', input);
            }
        }
    }

    private filterBarcodeValue(val: string): string {
        if (!val) {
        return val;
        }
        // Filter out extra characters permitted by HTML5 input type=number (for exponentials)
        const pattern = /[e|E|\+|\-|\.]/g;

        return val.toString().replace(pattern, '');
    }

    onBarcodePaste(event: ClipboardEvent) {
        const content = event.clipboardData.getData('text/plain');
        const filteredContent = this.filterBarcodeValue(content);
        if (filteredContent !== content) {
        this.log.info(`Clipboard data contains invalid characters for barcode, suppressing pasted content '${content}'`);
        }
        return filteredContent === content;
    }
}
