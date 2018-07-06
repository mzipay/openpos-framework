
import { Component } from '@angular/core';

import { IMenuItem } from './../../common/imenuitem';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
  selector: 'app-till-summary',
  templateUrl: './till-summary.component.html',
  styleUrls: ['./till-summary.component.scss']
})
export class TillSummaryComponent extends PosScreen<any> {

    nextAction: IMenuItem;

    buildScreen(){
        this.nextAction = this.screen.nextAction;
    }

    onNextAction() {
        this.session.onAction(this.nextAction.action);
    }

}
