import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
    selector: '[appScreen]'
})
export class ScreenDirective {
    constructor(public viewContainerRef: ViewContainerRef) {
      console.log('We have a screen');
    }
}
