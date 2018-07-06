import { Directive, HostListener, Renderer2, ElementRef } from '@angular/core';
import 'hammerjs';
import 'hammer-timejs';
import { Subscription } from 'rxjs';
import { SessionService } from '../../services/session.service';
import { Configuration } from '../../configuration/configuration';

@Directive({
    selector: 'mat-card-content'
})
export class MimicScrollDirective {

    private screenSubscription: Subscription;
    private screen: any;

    constructor(private elRef: ElementRef, public session: SessionService, public renderer: Renderer2) {
        if (Configuration.mimicScroll) {
            this.renderer.listen(elRef.nativeElement, 'panmove', (event) => {
                this.elRef.nativeElement.scrollTop += event.deltaY / 10;
            });
        }

    }
}
