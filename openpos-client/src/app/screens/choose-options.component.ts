import { IScreen } from '../common/iscreen';
import { Component, OnInit, DoCheck } from '@angular/core';
import {SessionService} from '../session.service';


@Component({
  selector: 'app-choose-options',
  templateUrl: './choose-options.component.html'
})
export class ChooseOptionsComponent implements IScreen, OnInit, DoCheck {

  public optionItems: IOptionItem[];
  public promptText: string;
  private lastSequenceNum: number;

  constructor(public session: SessionService) {
  }

  show(session: SessionService) {
    console.log('Show invoked');
  }

  ngOnInit(): void {
  }

  ngDoCheck(): void {
    if (this.session.screen.sequenceNumber !== this.lastSequenceNum) {
      // Screen changed, re-init
      this.optionItems = this.session.screen.options;
      this.promptText = this.session.screen.promptText;
      this.lastSequenceNum = this.session.screen.sequenceNumber;
    }
  }

}

export interface IOptionItem {
    displayValue: string;
    value: string;
    enabled: boolean;
    selected: boolean;
}
