import { Component, Inject } from '@angular/core';
import { StartupService } from '../../services/startup.service';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
    templateUrl: './startup-failed.component.html',
    styleUrls: ['startup-failed.component.scss']
})
export class StartupFailedComponent {

    title = 'Startup Failed ...';
    error: string;
    messages: string[];

    constructor( @Inject(MAT_DIALOG_DATA) public data: any) {
        this.error = data.error;
        this.messages = data.messages;
    }
}
