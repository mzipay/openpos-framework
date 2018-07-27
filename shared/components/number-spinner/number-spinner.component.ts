import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FormControl } from '@angular/forms';
import { AbstractValueAccessor, MakeProvider } from '../abstract-value-accessor';

@Component({
    selector: 'app-number-spinner',
    templateUrl: './number-spinner.component.html',
    providers: [MakeProvider(NumberSpinnerComponent)],
    styleUrls: ['./number-spinner.component.scss']
})
export class NumberSpinnerComponent extends AbstractValueAccessor implements OnInit {
    @Input() min = 0;
    @Input() max = 100;
    @Input() step = 1;
    @Input() precision = 1;
    @Input() inputDisabled = false;
    @Input() maxlength: number;
    @Input() minlength: number;
    // @Output() onChange: EventEmitter<number> = new EventEmitter();

    numberPicker: FormControl;

    constructor() { super(); }

    ngOnInit() {
        this.numberPicker = new FormControl({ value: this.value, disabled: this.inputDisabled });
    }

    public increaseValue(): void {
        let currentValue: number = Number(this.numberPicker.value);
        if (currentValue < this.max) {
            currentValue = currentValue + this.step;
            if (this.precision != null) {
                currentValue = this.round(currentValue, this.precision);
            }
            // this.value = currentValue;
            this.numberPicker.setValue(currentValue);
        }
    }

    public decreaseValue(): void {
        let currentValue: number = Number(this.numberPicker.value);
        if (currentValue > this.min) {
            currentValue = currentValue - this.step;
            if (this.precision != null) {
                currentValue = this.round(currentValue, this.precision);
            }
            // this.value = currentValue;
            this.numberPicker.setValue(currentValue);
        }
    }

    private round(value: number, precision: number): number {
        const multiplier: number = Math.pow(10, precision || 0);
        return Math.round(value * multiplier) / multiplier;
    }
}
