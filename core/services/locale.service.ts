import { SessionService } from './session.service';
import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root',
  })
export class LocaleService {
    constructor(public sessionService: SessionService) {
    }

    getLocale(): string {
        return this.sessionService.getLocale();
    }
}


