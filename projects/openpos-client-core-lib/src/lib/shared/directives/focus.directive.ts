import { FocusService } from './../../core/services/focus.service';
import { Directive, Input, ElementRef, AfterContentInit } from '@angular/core';

@Directive({
    selector: '[appFocus]'
})
export class FocusDirective implements AfterContentInit {

    @Input()
    appFocus: boolean;

    constructor(private element: ElementRef, private focusService: FocusService) {
    }

    ngAfterContentInit(): void {
        if (this.appFocus) {
            // setTimeout(() => this.element.nativeElement.focus());
            this.focusService.requestFocus('appFocus', this.element.nativeElement);
        }
    }
}
