import { Directive, HostListener, Renderer2, ElementRef } from '@angular/core';
import 'hammerjs';
import 'hammer-timejs';
import { environment } from '../environments/environment.prod';

@Directive({
    selector: 'mat-card-content'
})
export class MimicScrollDirective {

    constructor(private elRef: ElementRef) {
    }

    @HostListener('panmove', ['$event'])
    onPan(event: any): void {
        if (environment.hasOwnProperty('mimicScroll') && environment.mimicScroll) {
            this.elRef.nativeElement.scrollTop += event.deltaY / 10;
        }
    }
}
