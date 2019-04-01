import { Injectable, ElementRef } from '@angular/core';
import { BehaviorSubject, combineLatest, Observable } from 'rxjs';
import { Logger } from './logger.service';

@Injectable({
    providedIn: 'root',
})
export class FocusService {

    focusKey: string;

    focusElement: ElementRef;

    constructor(private log: Logger) {
    }

    public requestFocus(key: string, element: ElementRef) {
        this.log.info('requested focus for ' + key);
        this.focusKey = key;
        this.focusElement = element;
    }

    public executeFocus() {
        this.log.info('executing focus for ' + this.focusKey);
        if (this.focusElement && this.focusElement.nativeElement) {
            setTimeout(() => this.focusElement.nativeElement.focus());
            this.focusElement = null;
            this.focusKey = null;
        }
    }
}
