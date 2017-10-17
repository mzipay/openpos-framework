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
    itemActionName: string;
    text: string;

    constructor(public session: SessionService) {
    }

    show(session: SessionService) {
    }

    ngOnInit(): void {
        this.items = this.session.screen.items;
        this.itemActionName = this.session.screen.itemActionName;
        this.text = this.session.screen.text;
    }

    onItemClick(item: IItem): void {
        this.session.response = item;
        this.session.onAction(this.itemActionName);
    }

}
