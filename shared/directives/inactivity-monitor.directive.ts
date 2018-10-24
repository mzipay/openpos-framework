import { Directive, HostListener, Input, Renderer2, ElementRef } from '@angular/core';
import { Configuration } from '../../configuration/configuration';
import { SessionService } from '../../core';


@Directive({
    // tslint:disable-next-line:directive-selector
    selector: '[inactivityMonitor]'
})
export class InactivityMonitorDirective {

    static lastKeepAliveFlushTime: number = new Date().getTime();

    constructor(private elRef: ElementRef, public renderer: Renderer2, private session: SessionService) {
        if (Configuration.useTouchListener) {
            this.renderer.listen(elRef.nativeElement, 'touchstart', (event) => {
                this.touchEvent(event);
            });
        }
    }

    @Input() keepAliveMillis = Configuration.keepAliveMillis;

    private timerHandle: any;
    private _timeoutActive;

    @HostListener('window:keydown', ['$event'])
    keyEvent(event: KeyboardEvent) {
        this.keepAlive();
    }

    @HostListener('window:click', ['$event'])
    mouseEvent(event: MouseEvent) {
        this.keepAlive();
    }

    touchEvent(event: TouchEvent) {
        this.keepAlive();
    }

    private keepAlive() {
        const now = new Date().getTime();
        const nextFlushTime = InactivityMonitorDirective.lastKeepAliveFlushTime + this.keepAliveMillis;
        if (now > nextFlushTime) {
            InactivityMonitorDirective.lastKeepAliveFlushTime = now;
            this.session.keepAlive();
        }
    }
}
