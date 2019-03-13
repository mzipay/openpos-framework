
import { Component } from '@angular/core';

import { IActionItem } from './../../core';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
  selector: 'app-till-summary',
  templateUrl: './till-summary.component.html',
  styleUrls: ['./till-summary.component.scss']
})
export class TillSummaryComponent extends PosScreen<any> {

    nextAction: IActionItem;

    buildScreen() {
        this.nextAction = this.screen.nextAction;
    }

    onNextAction() {
        this.session.onAction(this.nextAction.action);
    }

}
