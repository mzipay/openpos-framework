import { Component } from '@angular/core';
import { ScreenPart } from '../../decorators/screen-part.decorator';
import { ScreenPartComponent } from '../screen-part';
import { MessageProvider } from '../../providers/message.provider';
import { IMultipleFormOption } from './multiple-form-option.interface';
import { FormGroup } from '@angular/forms';
import { FormBuilder } from '../../../core/services/form-builder.service';
import { IFormElement } from '../../../core/interfaces/form-field.interface';

@ScreenPart({
    name: 'forms'
})
@Component({
  selector: 'app-multiple-form-part',
  templateUrl: './multiple-form-part.component.html',
  styleUrls: ['./multiple-form-part.component.scss']
})
export class MultipleFormPartComponent extends ScreenPartComponent<IMultipleFormOption[]> {

    forms: IMultipleFormOption[];
    formGroups: FormGroup[];

    public showOptions = true;
    public selectedOption: IMultipleFormOption;
    public selectedForm: FormGroup;
    constructor(message: MessageProvider, private formBuilder: FormBuilder) {
        super(message);
     }

    screenDataUpdated() {
        if ( !this.screenData ) {
            return;
        }
        this.forms = this.screenData;
        this.formGroups = [];
        this.forms.forEach( f => {
            this.formGroups.push(this.formBuilder.group(f.form));
        });
    }

    onMakeOptionSelection( formOption: IMultipleFormOption, formGroup: FormGroup): void {
        this.selectedOption = formOption;
        this.selectedForm = formGroup;
        this.showOptions = false;
    }

    onBackButtonPressed(): void {
     this.showOptions = true;
    }

    onFieldChanged(formElement: IFormElement, option: IMultipleFormOption, group: FormGroup) {
        if (formElement.valueChangedAction) {
            this.formBuilder.buildFormPayload(group, option.form);
            this.sessionService.onAction(formElement.valueChangedAction, this.screenData);
        }
    }

    onSubmitForm( option: IMultipleFormOption, group: FormGroup): void {
        this.sessionService.onAction(option.submitButton, this.formBuilder.buildFormPayload(group, option.form));
    }
}
