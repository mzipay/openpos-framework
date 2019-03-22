import { Injectable } from '@angular/core';
import { SessionService } from './session.service';
import { PersonalizationService } from './personalization.service';
import { Logger } from './logger.service';
import { Configuration } from './../../configuration/configuration';


@Injectable({
    providedIn: 'root',
})
export class ConfigurationService {

    constructor(private log: Logger, private sessionService: SessionService, private personalization: PersonalizationService) {
        this.sessionService.getMessages('ConfigChanged').subscribe(m => this.updateConfig(m));
    }

    public updateConfig(message: any) {
        this.log.info(message);
        if (message && message.configuration) {
            this.mapConfig(message.configuration);
        }
        if (message && message.theme) {
            console.log('Config Changed Theme: ' + message.theme);
            this.personalization.setTheme(message.theme, true);
        }
    }

    protected mapConfig(response: any) {
        for (const p of Object.keys(response)) {
            if (Configuration.hasOwnProperty(p)) {
                Configuration[p] = response[p];
            }
        }
    }
}
