import { Directive, ElementRef, EventEmitter, HostListener, Input, OnDestroy, Optional, Output, Self } from '@angular/core';
import { MatInput } from '@angular/material';

import { MatKeyboardRef, MatKeyboardService, MatKeyboardComponent } from '@ngx-material-keyboard/core';

import { NgControl } from '@angular/forms';



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
    @Optional() @Self() private _control?: NgControl) { }

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

      // Massive HACK!!!! this is to fix a but in the Mat Keyboard library we are using
      // and should be removed whenever it gets fixed.
      if (!this._elementRef.nativeElement.value) {
        this._elementRef.nativeElement.value = "";
        let inputEvent = new Event("input");
        this._elementRef.nativeElement.dispatchEvent(inputEvent);
      }

      // reference the input element
      this._keyboardRef.instance.setInputInstance(this._elementRef);

      // set control if given, cast to smth. non-abstract
      if (this._control) {
        this._keyboardRef.instance.attachControl(this._control.control);
      }

      // connect outputs
      this._keyboardRef.instance.enterClick.subscribe(() => {
        let event = new Event("submit", { cancelable: true, bubbles: true });
        if (this._elementRef.nativeElement.form) {
          this._elementRef.nativeElement.form.dispatchEvent(event);
        }
        let enterPressedEvent = new KeyboardEvent("keypress", { key: "Enter", code: "Enter", cancelable: true, bubbles: true });
        this._elementRef.nativeElement.dispatchEvent(enterPressedEvent);
        let enterDownEvent = new KeyboardEvent("keydown", { key: "Enter", code: "Enter", cancelable: true, bubbles: true });
        this._elementRef.nativeElement.dispatchEvent(enterDownEvent);
        let enterUpEvent = new KeyboardEvent("keyup", { key: "Enter", code: "Enter", cancelable: true, bubbles: true });
        this._elementRef.nativeElement.dispatchEvent(enterUpEvent);

        this.enterClick.next();
      });
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
