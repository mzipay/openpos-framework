import { Component, OnInit, OnDestroy } from '@angular/core';
import { SessionService, IScreen, ActionIntercepter, ActionIntercepterBehaviorType, IActionItem } from '../../core';
import { IOptionItem } from '../../screens/choose-options/option-item.interface';
import { PosScreen } from '../../screens/pos-screen/pos-screen.component';

@Component({
  selector: 'app-self-checkout-options',
  templateUrl: './self-checkout-options.component.html',
  styleUrls: ['./self-checkout-options.component.scss']
})
export class SelfCheckoutOptionsComponent extends PosScreen<any> implements  OnInit, OnDestroy {

  static readonly UNDO = 'Undo';
  public currentView: string;
  public selectedOption: IOptionItem;
  public optionItems: IOptionItem[];

  constructor(public session: SessionService) {
      super();
  }

  buildScreen() {
    this.optionItems = this.screen.options;
    this.currentView = this.screen.displayStyle;
  }

  ngOnInit(): void {
  }

  ngOnDestroy() {
    this.session.unregisterActionIntercepter(SelfCheckoutOptionsComponent.UNDO);
  }

  onMakeOptionSelection(option: IOptionItem): void {
    if (option.form.formElements.length > 0) {
      this.selectedOption = option;
      this.currentView = 'OptionForm';
      this.session.registerActionIntercepter(SelfCheckoutOptionsComponent.UNDO,
        new ActionIntercepter(this.log, (payload) => { this.onBackButtonPressed(); }, ActionIntercepterBehaviorType.block));
    } else {
      this.session.onAction(option.value);
    }
  }

  onBackButtonPressed(): void {
    this.currentView = this.screen.displayStyle;
    this.session.unregisterActionIntercepter(SelfCheckoutOptionsComponent.UNDO);
  }

}
