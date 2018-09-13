import { Component, ViewChild, OnDestroy, AfterViewInit, OnInit } from '@angular/core';
import { MatInput } from '@angular/material';
import {
    IItem, IFormElement, IMenuItem, ValidatorsService, ActionIntercepter,
    ActionIntercepterBehavior, ActionIntercepterBehaviorType
} from '../../core';
import { PosScreen } from '../pos-screen/pos-screen.component';
import { FormGroup, ValidatorFn, FormControl } from '@angular/forms';

@Component({
    selector: 'app-tendering',
    templateUrl: './tendering.component.html'
})
export class TenderingComponent extends PosScreen<any> implements OnInit, AfterViewInit, OnDestroy {

    @ViewChild('tenderAmountField') tenderAmountField: MatInput;

    text: string;
    tenderItems: IItem[];
    tenderAmount: IFormElement;
    balanceDueAmount: IFormElement;
    balanceDue: string;
    totalAmount: IFormElement;
    itemActions: IMenuItem[] = [];
    actionButton: IMenuItem;

    tenderFormGroup: FormGroup;

    constructor(private validatorsService: ValidatorsService) {
        super();
    }

    public ngOnInit(): void {

    }

    ngAfterViewInit(): void {
        setTimeout(() => this.tenderAmountField.focus(), 0);
    }

    ngOnDestroy(): void {
        if (this.screen.template.localMenuItems) {
            this.screen.template.localMenuItems.forEach(element => {
                this.session.unregisterActionPayload(element.action);
                this.session.unregisterActionIntercepter(element.action);
            });
        }
    }

    buildScreen(): void {
        this.text = this.screen.text;
        this.tenderItems = this.screen.tenderItems;
        this.tenderAmount = this.screen.tenderAmount;

        const group: any = {};
        const validators: ValidatorFn[] = [];

        if (this.tenderAmount.validators) {
            this.tenderAmount.validators.forEach(v => {
                const fn = this.validatorsService.getValidator(v);
                if (fn) {
                    validators.push(fn);
                }
            });
        }

        group['tenderAmtFld'] = new FormControl(this.tenderAmount.value, validators);
        this.tenderFormGroup = new FormGroup(group);


        this.balanceDue = this.screen.balanceDue;
        this.balanceDueAmount = this.screen.balanceDueAmount;
        this.totalAmount = this.screen.totalAmount;
        this.itemActions = this.screen.itemActions;
        this.actionButton = this.screen.actionButton;

        if (this.screen.template.localMenuItems) {
            this.screen.template.localMenuItems.forEach(element => {
                this.session.registerActionPayload(element.action, () => this.tenderFormGroup.get('tenderAmtFld').value);
                this.session.registerActionIntercepter(element.action,
                    new ActionIntercepter(this.log, (payload) => {
                        const value = this.tenderFormGroup.get('tenderAmtFld').value;
                        this.log.info(`Returning value of ${value}.  Payload: ${JSON.stringify(payload)}`);
                        return value;
                    },
                        // Will only block if the formGroup is inValid
                        new ActionIntercepterBehavior(ActionIntercepterBehaviorType.block,
                            tenderAmtValue => this.tenderFormGroup.valid
                        )
                    )
                );
            });
            //                this.session.registerActionPayload( element.action, () => this.tenderFormGroup.get('tenderAmtFld').value );
            //            });
        }

    }

    onFormSubmit(): void {
        this.onAction();
    }

    onAction(): void {
        if (this.tenderFormGroup.valid) {
            this.tenderAmount.value = this.tenderFormGroup.get('tenderAmtFld').value;
            this.session.onAction(this.actionButton.action, this.tenderAmount.value);
        }
    }

}
