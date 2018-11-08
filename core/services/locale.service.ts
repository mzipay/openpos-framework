import { IMessageHandler } from './../interfaces/message-handler.interface';
import { SessionService } from './session.service';
import { Injectable, OnInit } from '@angular/core';

export const DEFAULT_LOCALE = 'en-US';
@Injectable({
    providedIn: 'root',
  })
export class LocaleService implements IMessageHandler<any>, OnInit {

    private locale = DEFAULT_LOCALE;

    constructor(public sessionService: SessionService) {
    }

    ngOnInit(): void {
        this.sessionService.registerMessageHandler(this);
    }
    handle(message: any) {
        if (message.locale) {
            this.locale = message.locale;
        }
        return this.locale;
    }

    getLocale(): string {
        return this.locale;
    }
}


