import { Directive, Input, ElementRef, forwardRef, Renderer2 } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { IFormatter } from './formatters/iformatter';

export const FORMATTED_INPUT_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => FormattedInputValueAccessor),
    multi: true
  };

@Directive({
    selector: 'input[InputFormatter]',
    host: { 
        '(onKeyPress)': 'onKeyPress($event)',
        '(input)': 'handleInput($event.target.value)'
     },
     providers: [FORMATTED_INPUT_VALUE_ACCESSOR]
})
export class FormattedInputValueAccessor implements ControlValueAccessor {

    @Input() InputFormatter: IFormatter;

    onChange = (value: string) => {};
    onTouched = () => {};

    constructor(private renderer: Renderer2, private elRef: ElementRef )  {
    }

    writeValue(value: string): void {
        if( value ){
            this.renderer.setProperty( this.elRef.nativeElement, 'value', this.InputFormatter.formatValue(value));
        }
    }

    registerOnChange(fn: (tel: string) => void ): void {
        this.onChange = fn;
    }

    registerOnTouched(fn: any): void {
        this.onTouched = fn;
    }

    handleInput(value: string){
        let cleanValue = this.InputFormatter.unFormatValue(value);
        this.renderer.setProperty( this.elRef.nativeElement, 'value', this.InputFormatter.formatValue(cleanValue));
        this.onChange(cleanValue);
    }

    onKeyPress(event: any){
        let inputChar = String.fromCharCode(event.charCode);

        if(!this.InputFormatter.keyFilter.test(inputChar) && event.charCode != 13){
            event.preventDefault();
        }
    }
}
