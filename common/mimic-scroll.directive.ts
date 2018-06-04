import { Directive, HostListener, Renderer2, ElementRef } from '@angular/core';
import 'hammerjs';
import 'hammer-timejs';
import { Subscription } from 'rxjs/Subscription';
import { SessionService } from '../services';

@Directive({
    selector: 'mat-card-content'
})
export class MimicScrollDirective {

    private screenSubscription: Subscription;
    private screen: any;

    constructor(private elRef: ElementRef, public session: SessionService, ) {
        this.screenSubscription = this.session.subscribeForScreenUpdates((screen: any): void => this.screen = screen);
    }

    @HostListener('panmove', ['$event'])
    onPan(event: any): void {
        if (this.screen && this.screen.mimicScroll) {
            this.elRef.nativeElement.scrollTop += event.deltaY / 10;
        }
    }
}
