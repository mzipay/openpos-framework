import { IScreen } from '../common/iscreen';
import { Component, OnInit, DoCheck, OnDestroy } from '@angular/core';
import {SessionService} from '../services/session.service';
import { AbstractApp } from '../common/abstract-app';
import { IForm } from './form.component';
import { ActionIntercepter, ActionIntercepterBehaviorType } from '../common/actionIntercepter';


@Component({
  selector: 'app-choose-options',
  templateUrl: './choose-options.component.html'
})
export class ChooseOptionsComponent implements IScreen, OnInit, DoCheck, OnDestroy {
  public currentView: string;
  public selectedOption: IOptionItem;
  public optionItems: IOptionItem[];
  private lastSequenceNum: number;
  public promptText: string;

  constructor(public session: SessionService) {
  }

  show(session: SessionService, app: AbstractApp) {
    console.log('Show invoked');
  }

  ngOnInit(): void {
  }

  ngDoCheck(): void {
    if (this.session.screen.sequenceNumber !== this.lastSequenceNum) {
      // Screen changed, re-init
      this.optionItems = this.session.screen.options;
      this.lastSequenceNum = this.session.screen.sequenceNumber;
      this.currentView = this.session.screen.displayStyle;
      this.promptText = this.session.screen.promptText;
    }
  }

  ngOnDestroy() {
    this.session.unregisterActionIntercepters();
  }

  onMakeOptionSelection( option: IOptionItem): void {
    if ( option.form.formElements.length > 0 ) {
      this.selectedOption = option;
      this.currentView = 'OptionForm';
      this.session.registerActionIntercepter("undo", new ActionIntercepter(this.onBackButtonPressed, ActionIntercepterBehaviorType.block));
    } else {
      this.session.onAction( option.value );
    }
  }

  onBackButtonPressed(): void {
    this.currentView = this.session.screen.displayStyle;
  }

}

export interface IOptionItem {
    displayValue: string;
    value: string;
    enabled: boolean;
    selected: boolean;
    form: IForm;
}
