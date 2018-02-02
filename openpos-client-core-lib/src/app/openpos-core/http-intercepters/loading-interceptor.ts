import { Injectable } from '@angular/core';
import {
    HttpEvent, HttpInterceptor, HttpHandler, HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { LoaderService } from '../common/loader/loader.service';
import { finalize } from 'rxjs/operators';

@Injectable()
export class LoadingInterceptor implements HttpInterceptor {
    constructor( private loaderService: LoaderService){

    }
    intercept( req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
  
        setTimeout(() => this.loaderService.show(), 1000);
        return next.handle(req).pipe(
            finalize(() => {
                this.loaderService.hide();
              })
        );
    }
}