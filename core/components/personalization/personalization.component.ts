import { Logger } from './../../services/logger.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { SessionService, PingResult } from '../../services';
import { IScreen } from '../dynamic-screen/screen.interface';
import { PersonalizationService } from '../../services/personalization.service';

@Component({
    selector: 'app-personalization',
    templateUrl: './personalization.component.html'
})
export class PersonalizationComponent implements IScreen, OnInit {

    firstFormGroup: FormGroup;
    secondFormGroup: FormGroup;
    checkTimeout: any;

    constructor(private log: Logger, private personalization: PersonalizationService, private session: SessionService, private formBuilder: FormBuilder, private http: HttpClient) {
    }

    ngOnInit() {
        this.firstFormGroup = this.formBuilder.group({
            serverName: ['', [Validators.required]],
            serverPort: ['', [Validators.required, , Validators.pattern('^[0-9]+$')]],
            sslEnabled: ['']
        }, { asyncValidator: this.serverValidator });
        this.secondFormGroup = this.formBuilder.group({
            storeNumber: ['', [Validators.required, , Validators.pattern('\\d{5}')]],
            deviceNumber: ['', [Validators.required, , Validators.pattern('\\d{3}')]]
        });
    }

    show(screen: any): void {
    }

    public personalize() {
        this.personalization.personalize(this.firstFormGroup.get('serverName').value, this.firstFormGroup.get('serverPort').value,
            {storeId: this.secondFormGroup.get('storeNumber').value, deviceId: this.secondFormGroup.get('deviceNumber').value},
            this.firstFormGroup.get('sslEnabled').value);
    }

    serverValidator = async (control: AbstractControl) => {
        clearTimeout(this.checkTimeout);
        const serverName = control.get('serverName').value;
        const serverPort = control.get('serverPort').value;
        const sslEnabled: boolean = control.get('sslEnabled').value;

        return new Promise((resolve, reject) => {
            this.checkTimeout = setTimeout(async () => {
                const result = await this.session.ping({serverName: serverName, serverPort: serverPort, useSsl: sslEnabled});
                if (result.success) {
                    resolve(null);
                } else {
                    this.log.info(`Ping failed with error: ${result.message}`);
                    resolve(result);
                }
            }, 1000);
        });
    }

}

