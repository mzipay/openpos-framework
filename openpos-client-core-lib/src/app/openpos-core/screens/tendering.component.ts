import { IMenuItem } from './../common/imenuitem';
import { IItem } from './../common/iitem';
import { Component, ViewChild, DoCheck, OnInit, OnDestroy } from '@angular/core';
import { SessionService } from '../services/session.service';
import { IScreen } from '../common/iscreen';
import createNumberMask from 'text-mask-addons/dist/createNumberMask';
import { IFormElement } from '../common/iformfield';
import { AbstractApp, AbstractTemplate } from '..';

@Component({
    selector: 'app-tendering',
    templateUrl: './tendering.component.html'
  })
  export class TenderingComponent implements IScreen, OnInit, OnDestroy {


    screen: any;
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

    }

    ngOnDestroy(): void {
        this.session.unregisterActionPayloads();
    }

    show(screen: any, app: AbstractApp, template?: AbstractTemplate): void {
        this.screen = screen;

        this.text = this.screen.text;
        this.tenderItems = this.screen.tenderItems;
        this.tenderAmount = this.screen.tenderAmount;
        this.balanceDue = this.screen.balanceDue;
        this.itemActions = this.screen.itemActions;

        this.screen.localMenuItems.forEach(element => {
            this.session.registerActionPayload( element.action, () => this.tenderAmount.value );
        });
    }

}
