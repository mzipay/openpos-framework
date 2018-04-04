import { Directive, Input, ElementRef } from '@angular/core';

@Directive({
    selector: '[autoSelectOnFocus]',
    host: { '(focus)': 'onFocus($event)' },
})
export class AutoSelectOnFocus {

    private element: ElementRef;

    constructor(el: ElementRef) {
        this.element = el;
    }

    onFocus($event: Event){
        this.element.nativeElement.select();
    }
}
