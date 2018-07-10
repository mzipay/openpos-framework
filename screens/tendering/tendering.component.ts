
import { Component, ViewChild, OnDestroy, AfterViewInit } from '@angular/core';
import { MatInput } from '@angular/material';
import { IItem, IFormElement, IMenuItem } from '../../core';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
    selector: 'app-tendering',
    templateUrl: './tendering.component.html'
  })
  export class TenderingComponent extends PosScreen<any> implements AfterViewInit, OnDestroy{

    @ViewChild('tenderAmountField') tenderAmountField: MatInput;

    text: string;
    tenderItems: IItem[];
    tenderAmount: IFormElement;
    balanceDueAmount: IFormElement;
    balanceDue: string;
    totalAmount: IFormElement;
    itemActions: IMenuItem[] = [];
    actionButton: IMenuItem;
    
    constructor() {
        super();
    }

    ngAfterViewInit(): void {
        setTimeout(() => this.tenderAmountField.focus(), 0); 
    }

    ngOnDestroy(): void {
        this.session.unregisterActionPayloads();
    }

    buildScreen(): void {
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
