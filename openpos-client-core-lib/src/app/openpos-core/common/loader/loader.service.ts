import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

import { LoaderState } from './loader';

@Injectable()
export class LoaderService {

    private loaderSubject = new Subject<LoaderState>();
    private currentLoaderState: LoaderState = { show : false, message:null, title:null};
    private oneTimeLoaderScreenBlock: boolean = false;

    loaderState = this.loaderSubject.asObservable();

    constructor() { }

    show() {
        if( this.oneTimeLoaderScreenBlock ){
            this.oneTimeLoaderScreenBlock = false;
            return;
        }
        
        this.currentLoaderState.show = true;

        this.loaderSubject.next(this.currentLoaderState);
    }

    hide() {
        this.currentLoaderState.show = false;
        this.currentLoaderState.message = null;
        this.currentLoaderState.title = null;

        this.loaderSubject.next(this.currentLoaderState);
    }

    setLoaderText(title?: string, message?: string ){
        this.currentLoaderState.message = message;
        this.currentLoaderState.title = title;

        this.loaderSubject.next( this.currentLoaderState );
    }

    setOneTimeLoaderScreenBlock(): void {
        this.oneTimeLoaderScreenBlock = true;
    }
}
