import { IMenuItem } from './../common/imenuitem';
import { IItem } from './../common/iitem';
import { Component, ViewChild, AfterViewInit, DoCheck, OnInit, OnDestroy, ElementRef } from '@angular/core';
import { SessionService } from '../services/session.service';
import { IScreen } from '../common/iscreen';
import createNumberMask from 'text-mask-addons/dist/createNumberMask';
import { IFormElement } from '../common/iformfield';

@Component({
    selector: 'app-tendering',
    templateUrl: './tendering.component.html'
  })
  export class TenderingComponent implements DoCheck, IScreen, OnInit, OnDestroy, AfterViewInit {

    private lastSequenceNum: number;

    @ViewChild('input') field: ElementRef;

    text: string;
    tenderItems: IItem[];
    tenderAmount: IFormElement;
    balanceDue: string;
    itemActions: IMenuItem[] = [];
    numberMask = createNumberMask({
        prefix: '',
        includeThousandsSeparator: false,
        allowDecimal: true,
        integerLimit: 9,
        requireDecimal: true,
        allowNegative: false
    });

    constructor(public session: SessionService) {
    }

    ngOnInit(): void {
        this.text = this.session.screen.text;
        this.tenderItems = this.session.screen.tenderItems;
        this.tenderAmount = this.session.screen.tenderAmount;
        this.balanceDue = this.session.screen.balanceDue;
        this.itemActions = this.session.screen.itemActions;

        this.session.screen.localMenuItems.forEach(element => {
            this.session.registerActionPayload( element.action, () => this.tenderAmount.value );
        });
    }

    ngAfterViewInit(): void {
        this.field.nativeElement.focus();
        this.field.nativeElement.select();
    }

    ngDoCheck(): void {
        // re-init the model if the screen is being redisplayed
        if (this.session.screen.type === 'Tendering'
            && this.session.screen.sequenceNumber !== this.lastSequenceNum) {
            this.ngOnInit();
            this.lastSequenceNum = this.session.screen.sequenceNumber;
        }
    }

    ngOnDestroy(): void {
        this.session.unregisterActionPayloads();
    }

    show(session: SessionService) {
    }
}
