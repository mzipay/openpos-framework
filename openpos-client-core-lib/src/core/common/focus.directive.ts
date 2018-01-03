import { Directive, Input, ElementRef, OnChanges } from '@angular/core';

@Directive({
    selector: '[appFocus]'
})
export class FocusDirective implements OnChanges {

    @Input()
    appFocus: boolean;

    constructor(private element: ElementRef) {
    }

    ngOnChanges(): void {
        if (this.appFocus) {
            this.element.nativeElement.focus();
        }
    }
}
