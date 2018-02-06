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
    private loading: boolean = false;
    intercept( req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
  
        this.loading = true;
        setTimeout(() => this.show(), 1000);
        return next.handle(req).pipe(
            finalize(() => {
                this.loading = false;
                this.loaderService.hide();
              })
        );
    }

    private show(){
        if( this.loading ){
            this.loaderService.show();
        }
    }
}