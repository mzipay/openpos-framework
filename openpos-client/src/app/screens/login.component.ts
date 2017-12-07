import { IFormElement, IForm } from './form.component';
import { SessionService } from './../services/session.service';
import { IScreen } from './../common/iscreen';
import { DoCheck, OnInit, Component } from '@angular/core';
@Component({
    selector: 'app-login',
    templateUrl: './login.component.html'
  })
  export class LoginComponent implements DoCheck, IScreen, OnInit {

    private lastSequenceNum: number;
    public form: IForm;
    loginIdField: IFormElement;
    passwordField: IFormElement;
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
        this.title = this.form.name;
    }

    ngDoCheck(): void {
        if (this.session.screen.sequenceNumber !== this.lastSequenceNum) {
            this.ngOnInit();
            this.lastSequenceNum = this.session.screen.sequenceNumber;
        }
    }

    onOkButton() {
        this.session.response = this.form;
        this.session.onAction(this.okButton.buttonAction);
    }
}

