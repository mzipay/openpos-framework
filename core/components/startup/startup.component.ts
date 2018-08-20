import { Component, OnInit, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { SessionService, DialogService } from '../../services';
import { IScreen } from '../dynamic-screen/screen.interface';
import { StartupService, StartupStatus } from '../../services/startup.service';

@Component({
    selector: 'app-startup',
    templateUrl: './startup.component.html',
    styleUrls: ['startup.component.scss']
})
export class StartupComponent implements IScreen, OnInit, AfterViewInit {

    title = 'Initializing ...';
    _messages: string[] = [];
    startupFailed = false;

    constructor(public dialog: DialogService, public startup: StartupService) {
    }

    ngOnInit(): void {
        this.startup.onStartupCompleted.subscribe(status => {
            if (status === StartupStatus.Failure) {
                this.startupFailed = true;
            }
        });
    }

    set message(msg: string) {
        this._messages.push(msg);
    }

    get message(): string {
        if (this._messages.length > 0) {
            return this._messages.slice(-1)[0];
        } else {
            return null;
        }
    }

    getAllMessages(): string[] {
        return this._messages;
    }

    log(msg: string) {
        console.log(msg);
        this.message = msg;
    }

    ngAfterViewInit(): void {
        setTimeout(() => {
           this.startup.runTasks(this);
           this.dialog.closeDialog();
        }, 500);
    }

    show(screen: any): void {
    }

}

