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
        // This is a hack, but for some reason on the tendering screen it focus but doesn't select the first time the page loads
        // It works every time after that when you focus on the input and it works on other pages, and setting a timeout seems to make it
        // work and I don't want to waste more time on this. Maybe someday we will find out way and can make this work without the timeout 
        setTimeout(() => this.element.nativeElement.select(), 0);
    }
}
