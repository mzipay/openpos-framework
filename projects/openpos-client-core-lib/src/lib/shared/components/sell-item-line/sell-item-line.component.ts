import { AppInjector } from './../../../core/app-injector';
import { SessionService } from './../../../core/services/session.service';
import { IActionItem } from './../../../core/interfaces/menu-item.interface';
import { Component, ViewChild, ElementRef, Input } from '@angular/core';
import { ISellItem } from '../../../core/interfaces/sell-item.interface';
import { Session } from 'electron';

@Component({
    selector: 'app-sell-item-line',
    templateUrl: './sell-item-line.component.html',
    styleUrls: ['./sell-item-line.component.scss']
})
export class SellItemLineComponent {

    @Input() item: ISellItem;
    @Input() readOnly: boolean;
    @Input() hideButton: boolean;

    constructor(private session: SessionService) {
    }

    onMenuItemClick(menuItem: IActionItem, payload?: any) {
        if (menuItem.enabled) {
            this.session.onAction(menuItem, payload);
        }
    }

}
