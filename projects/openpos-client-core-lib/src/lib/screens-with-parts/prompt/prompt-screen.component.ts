import { IActionItem } from './../../core/interfaces/menu-item.interface';
import { FormGroup, FormControl, ValidatorFn, Validators } from '@angular/forms';
import { Component, AfterViewInit, OnInit } from '@angular/core';
import { PosScreen } from '../../screens-deprecated/pos-screen/pos-screen.component';
import { ScreenComponent } from '../../shared/decorators/screen-component.decorator';
import { PromptInterface } from './prompt.interface';
import { ValidatorsService } from '../../core/services/validators.service';

@ScreenComponent({
    name: 'Prompt'
})
@Component({
    selector: 'app-prompt-screen',
    templateUrl: './prompt-screen.component.html'
})
export class PromptScreenComponent extends PosScreen<PromptInterface> implements AfterViewInit, OnInit {
    initialized = false;
    promptFormGroup: FormGroup;
    instructions: string;
    inputControlName = 'promptInputControl';
    hiddenInputControlName = 'promptInputHiddenDateControl';

    constructor(private validatorsService: ValidatorsService) {
        super();
    }

    buildScreen() {
        this.instructions = this.screen.instructions;
    }

    ngOnInit(): void {

        const group: any = {};
        const validators: ValidatorFn[] = [];
        validators.push(Validators.required);
        validators.push(this.validatorsService.getValidator(this.screen.responseType.toString()));

        if (this.screen.validators) {
            this.screen.validators.forEach(v => validators.push(this.validatorsService.getValidator(v.toString())));
        }

        if (this.screen.minLength) {
            validators.push(Validators.minLength(this.screen.minLength));
        }

        if (this.screen.maxLength) {
            validators.push(Validators.maxLength(this.screen.maxLength));
        }

        if (this.screen.validationPattern) {
            validators.push(Validators.pattern(this.screen.validationPattern));
        }

        group[this.inputControlName] = new FormControl(this.screen.responseText, validators);
        // When showing a DATE, there is also a hidden field to handle picking of dates using
        // a date picker, need to add a FormControl for that also.
        if (this.screen.responseType && this.screen.responseType.toString() !== 'DatePartChooser' &&
            this.screen.responseType.toString().toLowerCase().indexOf('date') >= 0) {
            group[this.hiddenInputControlName] = new FormControl();
        }
        this.promptFormGroup = new FormGroup(group);
    }

    ngAfterViewInit(): void {
        this.initialized = true;
    }

    onAction(menuItm: IActionItem) {
        this.session.onAction(menuItm);
    }

    onFormSubmit(): void {
        if (this.promptFormGroup.valid) {
            const payload = this.promptFormGroup.value[this.inputControlName];
            if (this.screen.actionButton) {
                this.session.onAction(this.screen.actionButton.action, payload);
            }
        }
    }

}
