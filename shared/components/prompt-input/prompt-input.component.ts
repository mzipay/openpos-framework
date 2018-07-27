import { FormGroup } from '@angular/forms';
import { Component, Input, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { TextMask, IMaskSpec, ITextMask } from '../../textmask';
import { FabToggleButtonComponent } from '../fab-toggle-button/fab-toggle-button.component';

@Component({
    selector: 'app-prompt-input',
    templateUrl: './prompt-input.component.html'
})

export class PromptInputComponent implements OnInit {

    @Input() placeholderText: string;
    @Input() responseType: string;
    @Input() responseText: string;
    @Input() promptIcon: string;
    @Input() hintText: string;
    @Input() maskSpec: IMaskSpec;
    @Input() minLength: number;
    @Input() maxLength: number;
    @Input() promptFormGroup: FormGroup;
    @Input() readOnly = false;
    @Input() keyboardPreference: string;

    inputType: string;
    checked = true;

    formatter: string;
    _textMask: ITextMask; // Mask object built for text-mask

    constructor(private datePipe: DatePipe) {
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

    onCheck() {
        if (this.responseText === 'ON') {
            this.responseText = 'OFF';
        } else {
            this.responseText = 'ON';
        }
    }
    ngOnInit(): void {
        this.formatter = this.responseType;

        if (this.maskSpec) {
            const newMask = TextMask.instance(this.maskSpec);
            this._textMask = newMask;
        } else {
            this._textMask = TextMask.NO_MASK;
        }
    }

}
