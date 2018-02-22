import { GiftCodeFormatter } from './../common/formatters/giftcode-formatter';
import { LocaleService } from './locale.service';
import { Injectable } from '@angular/core';
import { IFormatter } from '../common/formatters/iformatter';
import { DoNothingFormatter } from '../common/formatters/donothing-formatter';
import { MoneyFormatter } from '../common/formatters/money-formatter';
import { PhoneUSFormatter } from '../common/formatters/phoneUS-formatter';
import { PhoneCAFormatter } from '../common/formatters/phoneCA-formatter';
import { NumericFormatter } from '../common/formatters/numeric-formatter';

@Injectable()
export class FormattersService {
    private formatters = new Map<string, Map<string,IFormatter>>();

    constructor( private localeService: LocaleService ) {
        let USFormatters = new Map<string, IFormatter>();
        USFormatters.set('phone', new PhoneUSFormatter());
        this.formatters.set('en-US', USFormatters);

        let CAFormatters = new Map<string, IFormatter>();
        CAFormatters.set('phone', new PhoneCAFormatter());
        // Some screens are dependent on 'ca' value, don't change.  If you have other
        // ca formatters, add a second entry in the map for them.
        this.formatters.set('ca', CAFormatters);

        // If there isn't a specific formatter for a given locale, we fall back these
        let NOLOCALEFormatters = new Map<string, IFormatter>();
        this.formatters.set('NO-LOCALE', NOLOCALEFormatters);
        // Default formatters if no locale specific
        const numericFormatter = new NumericFormatter();
        NOLOCALEFormatters.set('numeric', numericFormatter);
        NOLOCALEFormatters.set('numerictext', numericFormatter);
        NOLOCALEFormatters.set('giftcode', new GiftCodeFormatter());
        // Use USD formatter as default
        NOLOCALEFormatters.set('money', new MoneyFormatter());
    }

    getFormatter( name: string ): IFormatter{

        let locale = this.localeService.getLocale();
        if( name ){

            let lname = name.toLowerCase();
            // see if we have a validator map for the current locale
            //  and that locale has the validator we need
            if( this.formatters.get(locale) && this.formatters.get(locale).get(lname)) {
                return this.formatters.get(locale).get(lname);
            }

            if( this.formatters.get('NO-LOCALE') && this.formatters.get('NO-LOCALE').get(lname)){
                return this.formatters.get('NO-LOCALE').get(lname);
            }
        }

        console.log( `No formatter found for locale ${locale} formatter name ${name}. Using and Do Nothing formatter`);
        return new DoNothingFormatter();
    }

 
}
