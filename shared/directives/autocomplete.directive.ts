import { Directive, ElementRef } from '@angular/core';
import { Configuration } from '../..';

@Directive({
    // tslint:disable-next-line:directive-selector
    selector: 'input:not([type=checkbox]), textarea, form'
})
export class AutocompleteDirective {

    constructor(public elementRef: ElementRef) {
        if (!Configuration.enableAutocomplete) {
            elementRef.nativeElement.setAttribute('autocomplete', 'off');
        }
    }
}
