import { Directive, Input, Renderer2, ElementRef, OnDestroy } from '@angular/core';
import { IActionItem, SessionService } from '../../core';
import { KeyPressProvider } from '../providers/keypress.provider';
import { Subscription } from 'rxjs';
import { Configuration } from '../../configuration/configuration';

@Directive({
    // tslint:disable-next-line:directive-selector
    selector: '[actionItem]'
})
export class ActionItemKeyMappingDirective implements OnDestroy {

    @Input()
    set actionItem(item: IActionItem) {
        if ( this.subscription ) {
            this.subscription.unsubscribe();
        }

        this.subscription = this.keyPresses.subscribe( item.keybind, 100, event => {
            // ignore repeats
            if ( event.repeat || !Configuration.enableKeybinds ) {
                return;
            }
            if ( event.type === 'keydown') {
                this.renderer.addClass(this.el.nativeElement, 'key-mapping-active');
                this.session.onAction(item);
            } else if ( event.type === 'keyup') {
                this.renderer.removeClass(this.el.nativeElement, 'key-mapping-active');
            }
        });
    }

    private subscription: Subscription;

    constructor(
        private renderer: Renderer2,
        private el: ElementRef,
        private session: SessionService,
        private keyPresses: KeyPressProvider) {

    }

    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }

}
