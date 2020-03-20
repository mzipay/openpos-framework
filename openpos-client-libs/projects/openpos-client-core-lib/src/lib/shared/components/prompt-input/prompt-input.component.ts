import { Logger } from './../../../core/services/logger.service';
import { Subscription } from 'rxjs';
import { FormGroup, FormControl, FormGroupDirective, NgForm } from '@angular/forms';
import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ErrorStateMatcher } from '@angular/material';
import { ScannerService } from '../../../core/platform-plugins/scanners/scanner.service';
import { SessionService } from '../../../core/services/session.service';

@Component({
    selector: 'app-prompt-input',
    templateUrl: './prompt-input.component.html'
})

export class PromptInputComponent implements OnInit, OnDestroy {

    @Input() placeholderText: string;
    @Input() responseType: string;
    @Input() responseText: string;
    @Input() promptIcon: string;
    @Input() hintText: string;
    @Input() minLength: number;
    @Input() maxLength: number;
    @Input() promptFormGroup: FormGroup;
    @Input() readOnly = false;
    @Input() keyboardPreference: string;
    @Input() scanEnabled = false;

    inputType: string;
    checked = true;
    errorMatcher = new MyErrorStateMatcher();
    keyboardLayout = 'en-US';

    private barcodeEventSubscription: Subscription;
    private scanServiceSubscription: Subscription;

    constructor(private log: Logger, private datePipe: DatePipe,
        private session: SessionService, private scannerService: ScannerService) {
    }

    isNumericField(): boolean {
        if (this.responseType) {
            return ['numerictext', 'money', 'phone', 'postalCode', 'percent', 'percentint', 'income', 'decimal']
                .indexOf(this.responseType.toLowerCase()) >= 0 || this.keyboardPreference === 'Numeric';
        } else {
            return false;
        }
    }

    isDateField(): boolean {
        if (this.responseType) {
            return this.responseType.toLowerCase().indexOf('date') >= 0;
        } else {
            return false;
        }
    }

    isPassword(): boolean {
        return this.responseType && this.responseType.toLowerCase() === 'alphanumericpassword';
    }

    onCheck() {
        if (this.responseText === 'ON') {
            this.responseText = 'OFF';
        } else {
            this.responseText = 'ON';
        }
    }

    ngOnInit(): void {
        if (this.scanEnabled) {
            this.registerScanner();
        }

        this.setKeyboardLayout();

    }

    ngOnDestroy(): void {
        if (this.barcodeEventSubscription) {
            this.barcodeEventSubscription.unsubscribe();
        }
        this.unregisterScanner();
        this.scannerService.stopScanning();
    }

    onBecomingActive() {
        this.registerScanner();
    }

    onLeavingActive() {
        this.unregisterScanner();
    }

    isScanAllowed(): boolean {
        return this.scanEnabled &&
            ['numerictext', 'alphanumerictext'].indexOf(this.responseType.toLowerCase()) >= 0;
    }


    // This method is invoked when the user presses the Scan button on the field.
    // For device-based scan events, see the ngOnInit method.

    onScan(): void {
        this.scannerService.triggerScan();
    }

    private setFieldValue(value: any) {
        const patchGroup = {};
        patchGroup['promptInputControl'] = value;
        this.promptFormGroup.patchValue(patchGroup);
    }

    private setKeyboardLayout() {
        if (this.responseType) {
            if (['numerictext', 'money', 'phone', 'postalCode', 'percent', 'percentint', 'income', 'decimal']
                .indexOf(this.responseType.toLowerCase()) >= 0) {
                this.keyboardLayout = 'Numeric';
            } else if (this.responseType.toLowerCase() === 'email') {
                this.keyboardLayout = 'Email';
            }
        }
    }

    private registerScanner() {
        if (typeof this.scanServiceSubscription === 'undefined' || this.scanServiceSubscription === null) {
            this.scanServiceSubscription = this.scannerService.startScanning().subscribe(scanData => {
                this.session.onAction('Scan', scanData.data);
            });
        }
    }

    private unregisterScanner() {
        if (this.scanServiceSubscription) {
            this.scanServiceSubscription.unsubscribe();
            this.scanServiceSubscription = null;
        }
    }
}

export class MyErrorStateMatcher implements ErrorStateMatcher {
    isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
        const isSubmitted = form && form.submitted;
        return (control && (control.dirty && control.invalid));  // show error only when dirty and invalid
    }
}