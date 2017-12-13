import { IFormElement, IForm } from './form.component';
import { SessionService } from './../services/session.service';
import { IScreen } from './../common/iscreen';
import { DoCheck, OnInit, Component } from '@angular/core';
import { IMenuItem } from '../common/imenuitem';
@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
  })
  export class LoginComponent implements DoCheck, IScreen, OnInit {

    private lastSequenceNum: number;
    public form: IForm;
    loginIdField: IFormElement;
    passwordField: IFormElement;
    submitAction: IMenuItem;
    changePasswordAction: IMenuItem;
    forgotPasswordAction: IMenuItem;
    okButton: IFormElement;
    cancelButton: IFormElement;
    title: string;

    constructor(public session: SessionService) {
    }

    show(session: SessionService) {
    }

    ngOnInit(): void {
        this.lastSequenceNum = this.session.screen.sequenceNumber;
        this.form = this.session.screen.form;
        this.loginIdField = this.form.formElements.find((e) => e.id === 'userId');
        this.passwordField = this.form.formElements.find((e) => e.id === 'password');
        this.okButton = this.form.formElements.find((e) => e.id === 'okButton');
        this.cancelButton = this.form.formElements.find((e) => e.id === 'cancelButton');
        this.submitAction = this.session.screen.submitAction;
        this.forgotPasswordAction = this.session.screen.forgotPasswordAction;
        this.changePasswordAction = this.session.screen.changePasswordAction;
        this.title = this.form.name;
    }

    hasSubmitAction(): boolean {
        return this.submitAction !== null;
    }

    ngDoCheck(): void {
        if (this.session.screen.sequenceNumber !== this.lastSequenceNum) {
            this.ngOnInit();
            this.lastSequenceNum = this.session.screen.sequenceNumber;
        }
    }

    onEnterPressed(): void {
        if (this.hasSubmitAction()) {
            this.onSubmitAction();
        } else {
            this.onOkButton();
        }
    }

    onSubmitAction(): void {
        this.session.response = this.form;
        this.session.onAction(this.submitAction.action);
    }

    onOkButton(): void {
        this.session.response = this.form;
        this.session.onAction(this.okButton.buttonAction);
    }

    onChangePasswordAction(): void {
        this.session.response = this.form;
        this.session.onAction(this.changePasswordAction.action);
    }

    onForgotPasswordAction(): void {
        this.session.response = this.form;
        this.session.onAction(this.forgotPasswordAction.action);
    }
}

