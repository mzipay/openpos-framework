import { IMenuItem } from './../common/imenuitem';
import { PromptInputComponent } from './../common/controls/prompt-input.component';
import { IFormElement } from './form.component';
import { IItem } from './../common/iitem';
import { Component, ViewChild, AfterViewInit, DoCheck, OnInit } from '@angular/core';
import { SessionService } from '../session.service';
import { IScreen } from '../common/iscreen';

@Component({
    selector: 'app-tendering',
    templateUrl: './tendering.component.html'
  })
  export class TenderingComponent implements DoCheck, IScreen, OnInit {
    private lastSequenceNum: number;

    text: string;
    tenderItems: IItem[];
    tenderAmount: IFormElement;
    balanceDue: string;
    itemActions: IMenuItem[] = [];

    constructor(public session: SessionService) {
    }

    ngOnInit(): void {
        this.text = this.session.screen.text;
        this.tenderItems = this.session.screen.tenderItems;
        this.tenderAmount = this.session.screen.tenderAmount;
        this.balanceDue = this.session.screen.balanceDue;
        this.itemActions = this.session.screen.itemActions;
    }

    ngDoCheck(): void {
        // re-init the model if the screen is being redisplayed
        if (this.session.screen.type === 'Tendering'
            && this.session.screen.sequenceNumber !== this.lastSequenceNum) {
            this.ngOnInit();
            this.lastSequenceNum = this.session.screen.sequenceNumber;
        }
    }

    show(session: SessionService) {
    }

    onEnterTender(event: Event): void {

    }

    onMenuAction(action: string): void {
        this.session.response = this.tenderAmount;
        this.session.onAction(action);
    }
  }
