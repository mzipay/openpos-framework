import { IMenuItem } from './../../common/imenuitem';
import { IItem } from './../../common/iitem';
import { SessionService } from './../../services/session.service';
import { IScreen } from './../../common/iscreen';
import { Component, OnInit, ViewChild } from '@angular/core';
import { AbstractApp } from '../../common/abstract-app';
import createNumberMask from 'text-mask-addons/dist/createNumberMask';
import { OnDestroy } from '@angular/core/src/metadata/lifecycle_hooks';

@Component({
  selector: 'app-till-count-other-tender',
  templateUrl: './till-count-other-tender.component.html',
  styleUrls: ['./till-count-other-tender.component.scss']
})
export class TillCountOtherTenderComponent implements OnInit, OnDestroy, IScreen {
  @ViewChild('amountInput') amountField: any;

  amountValue: string;
  totalAmount: number;
  nextAction: IMenuItem;

  items: IItem[];
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

  show(session: SessionService, app: AbstractApp) {
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
    this.items = [];
    this.totalAmount = 0;
    this.nextAction = this.session.screen.nextAction;
  }

  public doMenuItemAction(menuItem: IMenuItem, payLoad: any) {
    if (menuItem.action === 'Remove') {
      this.totalAmount -= Number(this.items[payLoad].amount);
      this.items.splice(payLoad, 1);
    }
  }

  onEnterAmount(event: Event) {

      this.items.push( {
        id: `amount${this.items.length}`,
        index: this.items.length,
        amount: this.amountValue,
        description: '',
        subtitle: '',
        fields: null,
        selected: false
      });

      this.totalAmount += Number(this.amountValue);
      this.amountValue = '';
  }

  onNextAction() {
    this.session.response = this.totalAmount.toString();
    this.session.onAction(this.nextAction.action);
  }


}
