import { Injectable } from '@angular/core';
import { Observable, fromEvent } from 'rxjs';
import { FromEventTarget } from 'rxjs/internal/observable/fromEvent';

@Injectable({
    providedIn: 'root'
})
export class DomEventManager {
    createEventObserver( target: FromEventTarget<Event>, eventName: string, args?: EventListenerOptions ): Observable<Event> {
        return fromEvent( target, eventName, args);
    }
}
