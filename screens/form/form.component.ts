import { Component, Output, EventEmitter } from '@angular/core';
import { IFormElement, IMenuItem, IForm } from '../../core';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html'
})
export class FormComponent extends PosScreen<any> {

  public form: IForm;
  formButtons: IFormElement[];
  @Output() onFieldChanged = new EventEmitter<{formElement: IFormElement, event: Event}>();

  constructor() {
      super()
  }

  buildScreen() {
    this.form = this.screen.form;
    this.formButtons = this.screen.form.formElements.filter((e) => e.elementType === 'Button');
  }

  onFormElementChanged(formElement: IFormElement, event: Event): void {
    this.onFieldChanged.emit({formElement: formElement, event: event});
  }

  onEnter(value: string) {
    // If there is a button which is a submitButton, submit the form
    // with that button's action
    const submitButtons: IFormElement[] = this.form.formElements.filter(
      elem => elem.elementType === 'Button' && elem.submitButton);

    if (submitButtons.length > 0) {
      this.session.response = this.form;
      this.session.onAction(submitButtons[0].buttonAction);
    }
  }

  onItemAction(menuItem: IMenuItem, $event): void {
    this.session.response = this.form;
    this.session.onAction(menuItem.action, null, menuItem.confirmationMessage);
  }

  onButtonAction(action: string) {
    this.session.response = this.form;
    this.session.onAction(action);
  }

  onSubmitOptionSelected(formElement: IFormElement, valueIndex: number, event: Event) {
    if (formElement.selectedIndexes) {
      formElement.selectedIndexes = [valueIndex];
    }

    this.session.response = this.form;
    this.session.onAction(formElement.id);
  }

  getPlaceholderText(formElement: IFormElement) {
    let text = '';
    if (formElement.label) {
      text += formElement.label;
    }
    if (text && formElement.placeholder) {
      text = `${text} - ${formElement.placeholder}`;
    }

    return text;
  }
}
