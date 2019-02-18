import { Directive, Input, HostListener, Renderer2, ElementRef } from '@angular/core';
import { IActionItem, SessionService } from '../../core';

@Directive({
    // tslint:disable-next-line:directive-selector
    selector: '[actionItem]'
})
export class ActionItemKeyMappingDirective {
    @Input()
    actionItem: IActionItem;

    constructor(private renderer: Renderer2, private el: ElementRef, private session: SessionService) {

    }

    @HostListener('document:keydown', ['$event'])
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

    @HostListener('document:keyup', ['$event'])
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
