import { Component, Inject, ViewChildren, QueryList, ElementRef, Renderer2, OnDestroy } from '@angular/core';
import { MAT_DIALOG_DATA, MatButton } from '@angular/material';
import { KeyPressProvider } from '../../providers/keypress.provider';
import { Subscribable, Subscription } from 'rxjs';

@Component({
  selector: 'app-kebab-menu',
  templateUrl: './kebab-menu.component.html',
  styleUrls: ['./kebab-menu.component.scss']
})
export class KebabMenuComponent implements OnDestroy {

    @ViewChildren(MatButton, {read: ElementRef})
    buttons: QueryList<ElementRef>;

    private _subscription: Subscription;

    constructor( @Inject(MAT_DIALOG_DATA) public data: any, keyPresses: KeyPressProvider) {
        this._subscription = keyPresses.subscribe( 'ArrowUp', 1, (event: KeyboardEvent) => {

            if ( event.repeat || event.type !== 'keydown' ) {
                return;
            }
            const index = this.buttons.toArray().map(v => v.nativeElement).indexOf(document.activeElement);
            if ( index > 0) {
                console.log(event);
                this.buttons.toArray()[index - 1].nativeElement.focus();
            }
        });

        this._subscription.add( keyPresses.subscribe( 'ArrowDown', 1, (event: KeyboardEvent) => {
            if ( event.repeat || event.type !== 'keydown' ) {
                return;
            }
            const index = this.buttons.toArray().map(v => v.nativeElement).indexOf(document.activeElement);
            if ( index + 1 < this.buttons.length) {
                console.log(event);
                this.buttons.toArray()[index + 1].nativeElement.focus();
            }
        }));
    }

    ngOnDestroy(): void {
        if (this._subscription) {
            this._subscription.unsubscribe();
        }
    }

}
