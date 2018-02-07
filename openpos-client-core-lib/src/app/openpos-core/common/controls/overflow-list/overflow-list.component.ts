import { Component, Input, DoCheck } from '@angular/core';

@Component({
    selector: 'app-overflow-list',
    templateUrl: './overflow-list.component.html',
    styleUrls: ['./overflow-list.component.scss']
})
export class OverFlowListComponent implements DoCheck {

    @Input() numberItemsToShow: number;
    @Input() items: any[];

    shownItems: any[] = [];
    overflowItems: any[] = [];

    constructor(){

    }

    ngDoCheck(): void {
        this.shownItems = this.items.slice(0, this.numberItemsToShow);
        this.overflowItems = this.items.slice(this.numberItemsToShow);
    }
}