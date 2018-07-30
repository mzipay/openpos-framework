import { Component, OnInit, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { SessionService } from '../../services';
import { IScreen } from '../dynamic-screen/screen.interface';
import { StartupService } from '../../services/startup.service';

@Component({
    selector: 'app-startup',
    templateUrl: './startup.component.html'
})
export class StartupComponent implements IScreen, AfterViewInit {


    messages = [];

    constructor(public session: SessionService, startup: StartupService) {
    }

    ngAfterViewInit(): void {
        setTimeout(() => {

        }, 500);
    }

    show(screen: any): void {
    }

}

