import { Logger } from './logger.service';
import { Injectable } from '@angular/core';
import { LocaleService } from './locale.service';

// Since input-formatter directive references the formatter service we cannot
// import from the shared barrel here
import {
    IFormatter,
    PhoneUSFormatter,
    PhoneCAFormatter,
    NumericFormatter,
    GiftCodeFormatter,
    MoneyFormatter,
    PercentageFormatter,
    PostalCodeFormatter,
    IncomeFormatter,
    StateIDNumberFormatter,
    DecimalFormatter,
    WordTextFormatter,
    DoNothingFormatter,
    DateTimeFormatter,
    DateTimeCAFormatter
} from '../../shared/formatters';


@Injectable({
    providedIn: 'root',
  })
export class FormattersService {
    private formatters = new Map<string, Map<string, IFormatter>>();

    constructor(private log: Logger, private localeService: LocaleService) {
        const USFormatters = new Map<string, IFormatter>();
        const defaultPhoneFormatter = new PhoneUSFormatter();

        USFormatters.set('phone', defaultPhoneFormatter);
        this.formatters.set('en-us', USFormatters);
        this.formatters.set('us', USFormatters);

        const CAFormatters = new Map<string, IFormatter>();
        CAFormatters.set('phone', new PhoneCAFormatter());
        CAFormatters.set('datetime', new DateTimeCAFormatter());

        // Some screens are dependent on 'ca' value, so don't change.  If you have other
        // ca formatters that are language specific, add a second entry in the map for them.
        this.formatters.set('ca', CAFormatters);
        this.formatters.set('en-ca', CAFormatters);

        // If there isn't a specific formatter for a given locale, we fall back these
        const NOLOCALEFormatters = new Map<string, IFormatter>();
        this.formatters.set('NO-LOCALE', NOLOCALEFormatters);
        // Default formatters if no locale specific
        const numericFormatter = new NumericFormatter();
        NOLOCALEFormatters.set('numeric', numericFormatter);
        NOLOCALEFormatters.set('numerictext', numericFormatter);
        NOLOCALEFormatters.set('giftcode', new GiftCodeFormatter());
        // Use USD formatter as default
        NOLOCALEFormatters.set('money', new MoneyFormatter());
        NOLOCALEFormatters.set('phone', defaultPhoneFormatter);
        NOLOCALEFormatters.set('percent', new PercentageFormatter());
        NOLOCALEFormatters.set('percentint', new PercentageFormatter(PercentageFormatter.INTEGER_MODE));
        NOLOCALEFormatters.set('postalcode', new PostalCodeFormatter());
        NOLOCALEFormatters.set('uspostalcode', numericFormatter);
        NOLOCALEFormatters.set('income', new IncomeFormatter());
        NOLOCALEFormatters.set('stateidnumber', new StateIDNumberFormatter());
        NOLOCALEFormatters.set('decimal', new DecimalFormatter());
        NOLOCALEFormatters.set('wordtext', new WordTextFormatter());
        NOLOCALEFormatters.set('datetime', new DateTimeFormatter());
    }

    getFormatter(name: string): IFormatter {

        const locale = this.localeService.getLocale();
        if (name && locale) {
            const lname = name.toLowerCase();
            const llocale = locale.toLowerCase();
            // see if we have a validator map for the current locale
            //  and that locale has the validator we need
            if (this.formatters.get(llocale) && this.formatters.get(llocale).get(lname)) {
                return this.formatters.get(llocale).get(lname);
            }

            if (this.formatters.get('NO-LOCALE') && this.formatters.get('NO-LOCALE').get(lname)) {
                return this.formatters.get('NO-LOCALE').get(lname);
            }
        }

        this.log.info(`No formatter found for locale '${locale}' formatter name '${name}'. Using a 'Do Nothing' formatter`);
        return new DoNothingFormatter();
    }

    setFormatter(name: string, formatter: IFormatter, locale?: string) {
        if (!locale) {
            locale = 'NO-LOCALE';
        }

        this.formatters.get(locale).set(name, formatter);
    }
}
