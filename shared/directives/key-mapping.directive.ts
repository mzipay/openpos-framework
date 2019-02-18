import { Directive, Input, HostListener, Renderer2, ElementRef } from '@angular/core';
import { IActionItem, SessionService } from '../../core';

@Directive({
    // tslint:disable-next-line:directive-selector
    selector: '[keyMapping]'
})
export class KeyMappingDirective {
    @Input()
    actionItem: IActionItem;

    constructor(private renderer: Renderer2, private el: ElementRef, private session: SessionService) {

    }

    @HostListener('document:keydown', ['$event'])
    public onKeydown(event: KeyboardEvent) {
        let bound = false;
        if (this.actionItem.keybind === event.key ) {
            bound = true;
            this.renderer.addClass(this.el.nativeElement, 'mat-elevation-z24');
            this.renderer.addClass(this.el.nativeElement, 'active');
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
            this.renderer.removeClass(this.el.nativeElement, 'mat-elevation-z24');
            this.renderer.removeClass(this.el.nativeElement, 'active');
            this.session.onAction(this.actionItem);
        }
        if (bound) {
          event.preventDefault();
        }
    }
}
