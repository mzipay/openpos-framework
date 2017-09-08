import { Component,Input } from '@angular/core';
import {Location} from '@angular/common';
import {Router, NavigationStart} from '@angular/router'


@Component({
    selector: 'routing-listener',  
    template:'<div></div>'
  })
export class RoutingListener {
    constructor(router:Router) {
        router.events.subscribe(event => {
            if (event instanceof NavigationStart) {
                if (event && event.url == '/initial' && localStorage.getItem('lastUrl') != null) {
                    let url = localStorage.getItem('lastUrl');
                    router.navigateByUrl(url);
                }
                else if (event && event.url != undefined && event.url != '/undefined') {
                    localStorage.setItem('lastUrl', event.url);
              }
          }
        });
    }
}
