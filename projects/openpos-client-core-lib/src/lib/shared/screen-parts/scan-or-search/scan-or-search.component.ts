import { IActionItem } from './../../../core/interfaces/menu-item.interface';
import { Component, AfterViewInit, Input, ElementRef, Output, EventEmitter } from '@angular/core';
import { ScreenPartComponent } from '../screen-part';
import { ScanOrSearchInterface } from './scan-or-search.interface';
import { DeviceService } from '../../../core/services/device.service';
import { MessageProvider } from '../../providers/message.provider';
import { ScreenPart } from '../../decorators/screen-part.decorator';
import { OpenposMediaService } from '../../../core/services/openpos-media.service';
import { Observable } from 'rxjs';

@ScreenPart({
    name: 'scan'
})
@Component({
    selector: 'app-scan-or-search',
    templateUrl: './scan-or-search.component.html',
    styleUrls: ['./scan-or-search.component.scss']
})
export class ScanOrSearchComponent extends ScreenPartComponent<ScanOrSearchInterface> {

    public barcode: string;
    isMobile$: Observable<boolean>;

    @Input() defaultAction: IActionItem;
    @Output() change: EventEmitter<string> = new EventEmitter<string>();


    constructor( public devices: DeviceService, messageProvider: MessageProvider,
                 private elRef: ElementRef, mediaService: OpenposMediaService) {
        super(messageProvider);
        const mobileMap = new Map([
            ['xs', true],
            ['sm', false],
            ['md', false],
            ['lg', false],
            ['xl', false]
        ]);
        this.isMobile$ = mediaService.mediaObservableFromMap(mobileMap);
    }

    screenDataUpdated() {
    }

    public onEnter(): void {
        if (this.barcode && this.barcode.trim().length >= this.screenData.scanMinLength) {
            this.sessionService.onAction(this.screenData.scanActionName, this.barcode);
            this.barcode = '';
        } else if (this.defaultAction && this.defaultAction.enabled) {
            this.sessionService.onAction(this.defaultAction);
        }
    }

    public onValueChange($event: any): void {
        this.change.emit(this.barcode);
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
