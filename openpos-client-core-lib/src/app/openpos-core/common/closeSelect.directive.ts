import { Directive, ViewContainerRef, ViewChild } from '@angular/core';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import { MatSelect } from '@angular/material';

@Directive({
    selector: 'mat-select'
})
export class CloseSelectDirective {

    constructor(public select: MatSelect, private breakpointObserver: BreakpointObserver) {
        
        
        breakpointObserver.observe([
            Breakpoints.HandsetLandscape,
            Breakpoints.HandsetPortrait,
            Breakpoints.TabletLandscape,
            Breakpoints.TabletPortrait
          ]).subscribe(result => {
              this.closeSelect();
          });
    }

    closeSelect(){
        if( this.select ){
            this.select.close();
        }
    }

}
