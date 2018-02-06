import {Component, Input } from '@angular/core';
import { AbstractControlDirective, AbstractControl } from '@angular/forms';

@Component({
    selector: 'show-errors',
    template: `
    <ul *ngIf="shouldShowErrors()">
        <li style="color: red" *ngFor="let error of listOfErrors()">{{error}}</li>
    </ul>
    `,
})
export class ShowErrorsComponent {
    private static readonly errorMessages = {
        'requireAtleastOne': () => 'Atleast one field is required'
    };

    @Input()
    private control: AbstractControlDirective | AbstractControl;

    shouldShowErrors(): boolean {
        return this.control &&
        this.control.errors &&
        (this.control.dirty || this.control.touched);
    }

    listOfErrors(): string[] {
        return Object.keys(this.control.errors)
        .map(field => this.getMessage(field, this.control.errors[field]));
    }

    private getMessage(type: string, params: any) {
        return ShowErrorsComponent.errorMessages[type](params);
    }
}