import { Component, Input, OnInit, Output, EventEmitter, Optional, Inject, ViewChild, AfterViewInit, ElementRef } from '@angular/core';
import { DeviceService } from '../../../services/device.service';
import { SessionService } from '../../../services/session.service';
import { MatDialogRef, MAT_DIALOG_DATA, MatInput } from '@angular/material';
import { ScanSomethingData } from './scanSomthingData';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'app-scan-something',
  templateUrl: './scan-something.component.html',
  styleUrls: ['./scan-something.component.scss']
})
export class ScanSomethingComponent implements AfterViewInit {

  @ViewChild(MatInput)
  input: MatInput;

  @Input()
  scanSomethingData: ScanSomethingData;

  public barcode: string;

  @Input() minLength = 1;
  @Input() maxLength: number;

  constructor(private session: SessionService, public devices: DeviceService,
    @Optional() public dialogRef: MatDialogRef<ScanSomethingComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) public data: ScanSomethingData) {

    this.session.subscribeForScreenUpdates((screen: any): void => this.focusFirst());

    if (data) {
      this.scanSomethingData = data;
    }
  }

  public onEnter(): void {
    if (this.barcode && this.barcode.trim().length >= this.minLength) {
      this.session.onAction('Next', this.barcode);
      this.barcode = '';
      if (this.dialogRef) {
        this.dialogRef.close();
      }
    }
  }


  ngAfterViewInit(): void {
    this.focusFirst();
  }

  private focusFirst(): void {
    if (this.scanSomethingData && this.scanSomethingData.autoFocus) {
      this.input.focus();
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
      console.log(`Clipboard data contains invalid characters for barcode, suppressing pasted content '${content}'`);
    }
    return filteredContent === content;
  }
}
