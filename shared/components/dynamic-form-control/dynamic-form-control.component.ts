import { Component, ViewChildren, AfterViewInit, Input, QueryList } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { SessionService, ScreenService, IFormElement, IForm, FormBuilder } from '../../../core';
import { DynamicFormFieldComponent } from '../dynamic-form-field/dynamic-form-field.component';

@Component({
  selector: 'app-dynamic-form-control',
  templateUrl: './dynamic-form-control.component.html',
  styleUrls: ['./dynamic-form-control.component.scss']
})
export class DynamicFormControlComponent implements AfterViewInit {
  
  @ViewChildren(DynamicFormFieldComponent) children: QueryList<DynamicFormFieldComponent>;

  ngAfterViewInit() {
    // Delays less than 1 sec do not work correctly.
    this.display(1000);
  }

  public display(delay: number) {
    const nonReadonlyChildren = this.children.filter( child => {
        if(child.field){
          return child.field.readonly === false;
        }
        return false;
      });
  
      if( nonReadonlyChildren.length > 0 ) {
        setTimeout(() => nonReadonlyChildren[0].field.focus(), delay);
      }
  }
  
  @Input() 
  get screenForm(): IForm{
    return this._screenForm;
  }

  set screenForm( screenForm: IForm){
    this._screenForm = screenForm;
    this.buttons = new Array<IFormElement>();

    this.form = this.formBuilder.group(screenForm);

    screenForm.formElements.forEach(element => {
        if (element.elementType === 'Button') {
        this.buttons.push(element);
        }
    });

  }

  @Input() submitAction: string;

  @Input() submitButtonText = 'Next';

  @Input() 
  get alternateSubmitActions(): string[]{
    return this._alternateSubmitActions;
  }

  set alternateSubmitActions( actions: string[] ){
    this._alternateSubmitActions = actions;
    if (actions) {
      actions.forEach(action => {

        this.session.registerActionPayload(action, () => {
          if (this.form.valid) {
            this.formBuilder.buildFormPayload(this.form, this._screenForm);
            return this.session.response = this._screenForm;
          } else {
            // Show errors for each of the fields where necessary
            Object.keys(this.form.controls).forEach(f => {
              const control = this.form.get(f);
              control.markAsTouched({onlySelf: true});
            });
            throw Error('form is invalid');
          }
        });
      });
    }
  }

  form: FormGroup;

  buttons: IFormElement[];

  private _screenForm: IForm;
  private _alternateSubmitActions: string[];

  constructor(public session: SessionService, public screenService: ScreenService, private formBuilder: FormBuilder) {}

  submitForm() {
    if (this.form.valid) {
      this.formBuilder.buildFormPayload(this.form, this._screenForm);
      this.session.onAction(this.submitAction, this._screenForm);
    }
  }

  onFieldChanged(formElement: IFormElement) {
    if (formElement.valueChangedAction) {
        this.formBuilder.buildFormPayload(this.form, this._screenForm);
      this.session.onValueChange(formElement.valueChangedAction, this._screenForm );
    }
  }

  onButtonClick(formElement: IFormElement) {
    this.session.onAction(formElement.buttonAction);
  }
}

