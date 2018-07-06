
import { Component, OnDestroy } from '@angular/core';
import { ActionIntercepter, ActionIntercepterBehaviorType } from '../../common/action-intercepter';
import { IOptionItem } from './option-item.interface';
import { IChooseOptionsScreen } from './choose-options-screen.interface';
import { PosScreen } from '../pos-screen/pos-screen.component';


@Component({
  selector: 'app-choose-options',
  templateUrl: './choose-options.component.html'
})
export class ChooseOptionsComponent extends PosScreen<IChooseOptionsScreen> implements  OnDestroy {

  static readonly UNDO = 'Undo';
  public currentView: string;
  public selectedOption: IOptionItem;
  public optionItems: IOptionItem[];
  public promptText: string;

  constructor() {
      super()
  }

  buildScreen() {
    this.optionItems = this.screen.options;
    this.currentView = this.screen.displayStyle;
    this.promptText = this.screen.promptText;
  }

  ngOnDestroy() {
    this.session.unregisterActionIntercepter(ChooseOptionsComponent.UNDO);
  }

  onMakeOptionSelection( option: IOptionItem): void {
    if ( option.form.formElements.length > 0 ) {
      this.selectedOption = option;
      this.currentView = 'OptionForm';
      this.session.registerActionIntercepter(ChooseOptionsComponent.UNDO,
        new ActionIntercepter((payload) => { this.onBackButtonPressed(); }, ActionIntercepterBehaviorType.block));
    } else {
      this.session.onAction( option.value );
    }
  }

  onBackButtonPressed(): void {
    this.currentView = this.screen.displayStyle;
    this.session.unregisterActionIntercepter(ChooseOptionsComponent.UNDO);
  }

}
