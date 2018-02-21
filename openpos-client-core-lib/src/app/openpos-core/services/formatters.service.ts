import { GiftCodeFormatter } from './../common/formatters/giftcode-formatter';
import { PhoneFormatter } from './../common/formatters/phone-formatter';
import { ILocaleService } from './locale.service';
import { Injectable } from '@angular/core';
import { IFormatter } from '../common/formatters/iformatter';
import { DoNothingFormatter } from '../common/formatters/donothing-formatter';
import { MoneyFormatter } from '../common/formatters/money-formatter';

@Injectable()
export class FormattersService {
    private formatters = new Map<string, IFormatter>();
    private _locale: string;

    constructor() {
        this.formatters.set('phone', new PhoneFormatter());
        this.formatters.set('money', new MoneyFormatter());
        this.formatters.set('giftcode', new GiftCodeFormatter());

    }
    get locale(): string {
        return this._locale;
    }

    /** Locale can only be set once. */
    set locale(locale: string) {
        if (! this._locale) {
            this._locale = locale;
        }
    }

    getFormatter( name: string, locale?: string): IFormatter{
        if( name ){
            let lname = name.toLowerCase();
            if(this.formatters.has(lname)){
                const formatter = this.formatters.get(lname);
                if (locale) {
                    formatter.locale = locale;
                }
                return this.formatters.get(lname);
            }
        }

        return new DoNothingFormatter();
    }

 
}
