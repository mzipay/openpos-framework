import { SessionService } from './../services/session.service';
import { Injectable } from '@angular/core';
import {
    HttpEvent, HttpInterceptor, HttpHandler, HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { LoaderState } from '../common/loader/loader-state';
import { finalize } from 'rxjs/operators';

@Injectable()
export class LoadingInterceptor implements HttpInterceptor {

    constructor(private session: SessionService) {
    }

    private loading = false;

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        this.loading = true;
        setTimeout(() => this.show(), 1000);
        return next.handle(req).pipe(
            finalize(() => {
                this.loading = false;
                this.session.loaderState.setVisible(false);
            })
        );
    }

    private show() {
        if (this.loading) {
            this.session.loaderState.setVisible(true);
        }
    }
}
