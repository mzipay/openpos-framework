import { Component, OnInit } from '@angular/core';
import { SessionService } from '../session.service';
import { IScreen } from './../common/iscreen';
import { IItem } from './../common/iitem';

@Component({
    selector: 'app-item-list',
    templateUrl: './item-list.component.html'
})
export class ItemListComponent implements IScreen, OnInit {

    items: IItem[];

    constructor(public session: SessionService) {
    }

    show(session: SessionService) {
    }

    ngOnInit(): void {
        this.items = this.session.screen.items;
    }

    onItemClick(item: IItem): void {
        console.log(`onItemClick:${item.description}`);

    }

}
