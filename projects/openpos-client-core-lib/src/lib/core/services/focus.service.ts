import { Injectable, ElementRef, NgZone } from '@angular/core';
import { Logger } from './logger.service';

@Injectable({
    providedIn: 'root',
})
export class FocusService {

    focusGranted = false;

    constructor(private log: Logger, private ngZone: NgZone) {
    }

    public reset() {
        this.focusGranted = false;
    }

    public requestFocus(key: string, element: any) {
        if (!this.focusGranted) {
            this.focusGranted = true;
            this.log.info('requested focus for ' + key);
            this.ngZone.run(() => element.focus());
        }
    }

}
