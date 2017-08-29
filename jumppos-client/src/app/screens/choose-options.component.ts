import { IScreen } from '../common/iscreen';
import {Component} from '@angular/core';
import {SessionService} from '../session.service';


@Component({
  selector: 'app-choose-options',
  templateUrl: './choose-options.component.html'
})
export class ChooseOptionsComponent implements IScreen {

  public optionItems: IOptionItem[];
  public promptText: string;

  constructor(public session: SessionService) {
    this.optionItems = session.screen.options;
    this.promptText = session.screen.promptText;
  }

  show(session: SessionService) {
  }

}


export interface IOptionItem {
    displayValue: string;
    value: string;
    enabled: boolean;
    selected: boolean;
}
