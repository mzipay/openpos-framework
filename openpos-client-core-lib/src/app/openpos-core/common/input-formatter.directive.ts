import { Directive, Input, ElementRef, forwardRef, Renderer2, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { IFormatter } from './formatters/iformatter';
import { FormattersService } from './../services/formatters.service';
import { LocaleService } from '../services/locale.service';

export const FORMATTED_INPUT_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => FormattedInputValueAccessor),
    multi: true
  };

@Directive({
    selector: 'input[formatterName]',
    host: { 
        '(onKeyPress)': 'onKeyPress($event)',
        '(input)': 'handleInput($event.target.value)'
     },
     providers: [FORMATTED_INPUT_VALUE_ACCESSOR]
})
export class FormattedInputValueAccessor implements ControlValueAccessor, OnInit {

    @Input() formatterName: string;

    private formatter: IFormatter;

    onChange = (value: string) => {};
    onTouched = () => {};

    constructor(private renderer: Renderer2, private elRef: ElementRef, private formatterService: FormattersService,
        private localeService: LocaleService )  {
    }

    ngOnInit(): void {
        this.formatter = this.formatterService.getFormatter(this.formatterName, this.localeService.getLocale());
    }

    writeValue(value: string): void {
        if( value ){
            this.renderer.setProperty( this.elRef.nativeElement, 'value', this.formatter.formatValue(value));
        }
    }

    registerOnChange(fn: (tel: string) => void ): void {
        this.onChange = fn;
    }

    registerOnTouched(fn: any): void {
        this.onTouched = fn;
    }

    handleInput(value: string){
        let cleanValue = this.formatter.unFormatValue(value);
        let caret = this.elRef.nativeElement.selectionStart;

        let beforeCaretClean = this.formatter.unFormatValue(value.slice(0,caret));
        let beforeCaretFormatted = this.formatter.formatValue(beforeCaretClean);
        let newCaret = 0;
        let i = 0

        while( newCaret < beforeCaretFormatted.length && i < beforeCaretClean.length ){
            if( beforeCaretClean[i] === beforeCaretFormatted[newCaret] ){
                ++newCaret;
                ++i;
            } else {
                ++newCaret;
            }
        }

        this.renderer.setProperty( this.elRef.nativeElement, 'value', this.formatter.formatValue(cleanValue));
        this.renderer.setProperty( this.elRef.nativeElement, 'selectionStart', newCaret);
        this.renderer.setProperty( this.elRef.nativeElement, 'selectionEnd', newCaret);
        this.onChange(cleanValue);
    }

    onKeyPress(event: any){
        const inputChar = String.fromCharCode(event.charCode);

        if (this.formatter.keyFilter) {
            if (this.formatter.keyFilter instanceof RegExp) {
                if (!this.formatter.keyFilter.test(inputChar) && event.charCode != 13) {
                    event.preventDefault();
                }
            } else {
                const currentValue = this.elRef.nativeElement.value;
                if (this.formatter.keyFilter.filter(currentValue, inputChar)) {
                    event.preventDefault();
                }
            }
        }
    }
}
