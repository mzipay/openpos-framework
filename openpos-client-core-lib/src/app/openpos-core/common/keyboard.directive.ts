import { Directive, ElementRef, EventEmitter, HostListener, Input, OnDestroy, Optional, Output, Self } from '@angular/core';
import { MatInput } from '@angular/material';

import { MatKeyboardRef, MatKeyboardService, MatKeyboardComponent } from '@ngx-material-keyboard/core';

import { SessionService } from '../services/session.service';

@Directive({
    selector: 'input:not([type=checkbox]), textarea'
})
export class KeyboardDirective implements OnDestroy {

    private _keyboardRef: MatKeyboardRef<MatKeyboardComponent>;

    @Input() matKeyboard: string;

    @Input() darkTheme: boolean;

    @Input() duration: number;

    @Input() isDebug: boolean;

    @Output() enterClick: EventEmitter<void> = new EventEmitter<void>();

    @Output() capsClick: EventEmitter<void> = new EventEmitter<void>();

    @Output() altClick: EventEmitter<void> = new EventEmitter<void>();

    @Output() shiftClick: EventEmitter<void> = new EventEmitter<void>();

    constructor(public session: SessionService, private _elementRef: ElementRef,
        private _keyboardService: MatKeyboardService,
        @Optional() @Self() private _control?: MatInput) { }

    ngOnDestroy() {
        this._hideKeyboard();
    }

    @HostListener('focus', ['$event'])
    private _showKeyboard() {
        if (this.session.screen.useOnScreenKeyboard) {
            this._keyboardRef = this._keyboardService.open(this.matKeyboard, {
                darkTheme: this.darkTheme,
                duration: this.duration,
                isDebug: this.isDebug
            });

            // reference input
            this._keyboardRef.instance.setInputInstance(this._elementRef, this._control);

            // connect outputs
            this._keyboardRef.instance.enterClick.subscribe(() => this.enterClick.next());
            this._keyboardRef.instance.capsClick.subscribe(() => this.capsClick.next());
            this._keyboardRef.instance.altClick.subscribe(() => this.altClick.next());
            this._keyboardRef.instance.shiftClick.subscribe(() => this.shiftClick.next());
        }
    }

    @HostListener('blur', ['$event'])
    private _hideKeyboard() {
        if (this._keyboardRef) {
            this._keyboardRef.dismiss();
        }
    }

}