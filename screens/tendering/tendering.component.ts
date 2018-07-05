
import { Component, ViewChild, OnDestroy, AfterViewInit } from '@angular/core';
import { MatInput } from '@angular/material';
import { IScreen } from '../../common/iscreen';
import { IItem } from '../../common/iitem';
import { IFormElement } from '../../common/iformfield';
import { IMenuItem } from '../../common/imenuitem';
import { SessionService } from '../../services/session.service';

@Component({
    selector: 'app-tendering',
    templateUrl: './tendering.component.html'
  })
  export class TenderingComponent implements IScreen, AfterViewInit, OnDestroy{

    @ViewChild('tenderAmountField') tenderAmountField: MatInput;

    screen: any;
    text: string;
    tenderItems: IItem[];
    tenderAmount: IFormElement;
    balanceDueAmount: IFormElement;
    balanceDue: string;
    totalAmount: IFormElement;
    itemActions: IMenuItem[] = [];
    actionButton: IMenuItem;
    
    constructor(public session: SessionService) {
    }

    ngAfterViewInit(): void {
        setTimeout(() => this.tenderAmountField.focus(), 0); 
    }

    ngOnDestroy(): void {
        this.session.unregisterActionPayloads();
    }

    show(screen: any): void {
        this.screen = screen;

        this.text = this.screen.text;
        this.tenderItems = this.screen.tenderItems;
        this.tenderAmount = this.screen.tenderAmount;
        this.balanceDue = this.screen.balanceDue;
        this.balanceDueAmount = this.screen.balanceDueAmount;
        this.totalAmount = this.screen.totalAmount;
        this.itemActions = this.screen.itemActions;
        this.actionButton = this.screen.actionButton;

        if (this.screen.template.localMenuItems) {
            this.screen.template.localMenuItems.forEach(element => {
                this.session.registerActionPayload( element.action, () => this.tenderAmount.value );
            });
        }
    }

    onAction(): void {
        this.session.onAction(this.actionButton.action, this.tenderAmount.value);
    }
        
}
