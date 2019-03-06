import { Directive, Input, Renderer2, ElementRef, OnInit } from '@angular/core';
import { IActionItem, SessionService } from '../../core';
import { KeyPressProvider } from '../providers/keypress.provider';

@Directive({
    // tslint:disable-next-line:directive-selector
    selector: '[actionItem]'
})
export class ActionItemKeyMappingDirective implements OnInit {

    @Input()
    actionItem: IActionItem;

    constructor(
        private renderer: Renderer2,
        private el: ElementRef,
        private session: SessionService,
        private keyPresses: KeyPressProvider) {

    }

    ngOnInit(): void {
        this.keyPresses.getKeyPresses().subscribe( event => {
            if ( event.type === 'keydown') {
                this.onKeydown(event);
            } else if ( event.type === 'keyup') {
                this.onKeyup(event);
            }
        });
    }

    public onKeydown(event: KeyboardEvent) {
        let bound = false;
        if (this.actionItem.keybind === event.key ) {
            bound = true;
            this.renderer.addClass(this.el.nativeElement, 'key-mapping-active');
        }
        if (bound) {
          event.preventDefault();
        }
    }

    public onKeyup(event: KeyboardEvent) {
        let bound = false;
        if (this.actionItem.keybind === event.key ) {
            bound = true;
            this.renderer.removeClass(this.el.nativeElement, 'key-mapping-active');
            this.session.onAction(this.actionItem);
        }
        if (bound) {
          event.preventDefault();
        }
    }
}
