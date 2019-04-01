import { Injectable, ElementRef } from '@angular/core';
import { Logger } from './logger.service';

@Injectable({
    providedIn: 'root',
})
export class FocusService {

    focusGranted = false;

    constructor(private log: Logger) {
    }

    public reset() {
        this.focusGranted = false;
    }

    public requestFocus(key: string, element: any) {
        if (!this.focusGranted) {
            this.log.info('requested focus for ' + key);
            if (element.nativeElement) {
                setTimeout(() => element.nativeElement.focus(), 100);
            } else {
                setTimeout(() => element.focus(), 100);
            }
            this.focusGranted = true;
        }
    }

}
