import { Component, ViewChild, AfterViewChecked, ElementRef, OnInit } from '@angular/core';
import { IScreen } from '../../core/components/dynamic-screen/screen.interface';
import { ISellItem } from '../../core/interfaces/sell-item.interface';
import { SessionService } from '../../core/services/session.service';
import { IActionItem } from '../../core/interfaces/action-item.interface';

@Component({
    selector: 'app-self-checkout-transaction',
    templateUrl: './self-checkout-transaction.component.html',
    styleUrls: ['./self-checkout-transaction.component.scss']
})
export class SelfCheckoutTransactionComponent implements AfterViewChecked, IScreen, OnInit {

    public screen: any;
    @ViewChild('scrollList') private scrollList: ElementRef;

    initialized = false;

    public items: ISellItem[];
    public size = -1;

    constructor(public session: SessionService) {
    }

    show(screen: any) {
        this.screen = screen;

        this.items = this.screen.items;
    }

    ngOnInit(): void {
        this.scrollToBottom();
    }

    ngAfterViewChecked() {
        if (this.items && this.size !== this.items.length) {
            this.scrollToBottom();
            this.size = this.items.length;
        }
    }

    public doMenuItemAction(menuItem: IActionItem) {
        this.session.onAction(menuItem);
    }

    scrollToBottom(): void {
        try {
            this.scrollList.nativeElement.scrollTop = this.scrollList.nativeElement.scrollHeight;
        } catch (err) { }
    }

}
