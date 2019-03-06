import { Injectable } from '@angular/core';
import { Observable, merge, Subject, Subscription } from 'rxjs';

@Injectable()
export class KeyPressProvider {

    private keyPresses$ = new Subject<KeyboardEvent>();
    private keyPressSources: Observable<KeyboardEvent>[] = [];
    private subscription: Subscription;

    registerKeyPressSource( source$: Observable<KeyboardEvent>) {
        this.keyPressSources.push(source$);
        this.rebuildKeyPressObserver();
    }

    getKeyPresses(): Observable<KeyboardEvent> {
        return this.keyPresses$;
    }

    private rebuildKeyPressObserver() {
        if ( this.subscription ) {
            this.subscription.unsubscribe();
        }
        this.subscription = merge(...this.keyPressSources).subscribe( event => this.keyPresses$.next(event));
    }

}
