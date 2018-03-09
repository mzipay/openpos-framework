import { Directive, Input, ElementRef, forwardRef, Renderer2, OnInit } from '@angular/core';
import { MatSelectionList, MatListOption } from '@angular/material';

@Directive({
    selector: 'mat-selection-list[exclusiveSelect]',
    host: { 
        '(selectionChange)': 'onSelectionChange($event.option, $event.source)'
     }
})
export class MatExclusiveSelectionListDirective {
    onSelectionChange(option: MatListOption, source: MatSelectionList) {
        if(option.selected) {
            source.options.forEach(o=>
                {
                    if(o != option){
                        o.selected=false
                    }
                });
        }
    }
   
}
