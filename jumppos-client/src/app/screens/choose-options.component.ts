import {Component} from '@angular/core';
import {SessionService} from '../session.service';


@Component({
  selector: 'app-choose-options',
  templateUrl: './choose-options.component.html'
})
export class ChooseOptionsComponent {

  public optionItems: IOptionItem[];

  constructor(public session: SessionService) {
    this.optionItems = session.screen.options;
  }

}


export interface IOptionItem {
    displayValue: string;
    value: string;
    enabled: boolean;
    selected: boolean;
}
