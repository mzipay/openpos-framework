import { Directive, Input, ElementRef, OnInit } from '@angular/core';

@Directive({
    selector: '[appFocus]'
})
export class FocusDirective implements OnInit {

    @Input()
    appFocus: boolean;

    constructor(private element: ElementRef) {
    }

    ngOnInit(): void {
        if (this.appFocus) {
            this.element.nativeElement.focus();
        }
    }
}
