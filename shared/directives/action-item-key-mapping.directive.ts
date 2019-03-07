import { Directive, Input, Renderer2, ElementRef, OnInit, OnDestroy } from '@angular/core';
import { IActionItem, SessionService } from '../../core';
import { KeyPressProvider } from '../providers/keypress.provider';
import { Subscription } from 'rxjs';

@Directive({
    // tslint:disable-next-line:directive-selector
    selector: '[actionItem]'
})
export class ActionItemKeyMappingDirective implements OnDestroy, OnInit {

    @Input()
    actionItem: IActionItem;

    private subscription: Subscription;

    constructor(
        private renderer: Renderer2,
        private el: ElementRef,
        private session: SessionService,
        private keyPresses: KeyPressProvider) {

    }

    ngOnInit(): void {
        this.subscription = this.keyPresses.getKeyPresses().subscribe( event => {
            // ignore repeats
            if ( event.repeat ) {
                return;
            }
            if ( event.type === 'keydown') {
                this.onKeydown(event);
            } else if ( event.type === 'keyup') {
                this.onKeyup(event);
            }
        });
    }

    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }


    public onKeydown(event: KeyboardEvent) {
        if (this.actionItem.keybind === event.key ) {
            this.renderer.addClass(this.el.nativeElement, 'key-mapping-active');
            this.session.onAction(this.actionItem);
            event.preventDefault();
        }
    }

    public onKeyup(event: KeyboardEvent) {
        if (this.actionItem.keybind === event.key ) {
            this.renderer.removeClass(this.el.nativeElement, 'key-mapping-active');
            event.preventDefault();
        }
    }
}
