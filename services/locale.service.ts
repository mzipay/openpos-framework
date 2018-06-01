import { SessionService } from './session.service';
import { Injectable, Inject, forwardRef } from '@angular/core';

export const DEFAULT_LOCALE = 'en-US';

export interface ILocaleService {
    getLocale(): string;
}


export abstract class LocaleService implements ILocaleService {
    constructor(protected sessionService: SessionService) {
    }

    abstract getLocale(): string;
}

@Injectable()
export class LocaleServiceImpl extends LocaleService {
    constructor(@Inject(forwardRef(() => SessionService))sessionService: SessionService) {
        super(sessionService);
    }

    getLocale(): string {
        return this.sessionService.getLocale();
    }
}


