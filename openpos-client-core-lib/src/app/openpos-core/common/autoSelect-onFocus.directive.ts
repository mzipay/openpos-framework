import { Directive, Input, ElementRef, HostListener } from '@angular/core';

@Directive({
    selector: '[autoSelectOnFocus]'
})
export class AutoSelectOnFocus {

    private element: ElementRef;

    constructor(el: ElementRef) {
        this.element = el;
    }

    
    @HostListener('focus', ['$event'])
    onFocus($event: Event){
        this.element.nativeElement.select();
    }
}
