import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';

@Component({
    selector: 'app-overflow-list',
    templateUrl: './overflow-list.component.html',
    styleUrls: ['./overflow-list.component.scss']
})
export class OverFlowListComponent implements OnChanges {

    @Input() numberItemsToShow: number;
    @Input() items: any[];

    shownItems: any[];
    overflowItems: any[];

    constructor(){
        
    }

    showOverflowList(): boolean {
        return this.numberItemsToShow < this.items.length;
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.updateLists();
    }

    private updateLists(): void {
        this.shownItems = this.items.slice(0, this.numberItemsToShow);
        this.overflowItems = this.items.slice(this.numberItemsToShow);
    }
}