import { IFormatter } from './iformatter';

export class PhoneFormatter implements IFormatter {
    static readonly DEFAULT_FILTER_REGEX = /^[2-9]$|^[2-9][0-8]$|^[2-9][0-8][0-9]$|^[2-9][0-8][0-9][2-9]$|^[2-9][0-8][0-9][2-9][0-9]$|^[2-9][0-8][0-9][2-9][0-9]{2}$|^[2-9][0-8][0-9][2-9][0-9]{3}$|^[2-9][0-8][0-9][2-9][0-9]{4}$|^[2-9][0-8][0-9][2-9][0-9]{5}$|^[2-9][0-8][0-9][2-9][0-9]{6}$/;
    static readonly LOCALE_FILTER_MAP = new Map<string, RegExp>([
        ['us', PhoneFormatter.DEFAULT_FILTER_REGEX],
        ['ca', /^[0-9]{1,10}$/]
    ]);

    private _locale: string;

    private keyFilter = /[0-9]/;
    private newValueFilter = PhoneFormatter.DEFAULT_FILTER_REGEX;
    constructor() {
    }

    set locale(locl: string) {
        this._locale = locl;
        // cache the formater we'll use
        if (locl) {
            PhoneFormatter.LOCALE_FILTER_MAP.forEach((value, mapLocaleKey) => {
                // If given at least a portion of the mapkey is contained in the set locale,
                // that is the filter regex we will choose.
                if (mapLocaleKey.indexOf(this._locale.toLowerCase()) >= 0) {
                    this.newValueFilter = value;
                }
            });
        } else {
            this.newValueFilter = PhoneFormatter.DEFAULT_FILTER_REGEX;
        }

    }

    get locale(): string {
        return this._locale;
    }

    formatValue(value: string): string {
        if (!value) {
            return '';
        }

        if (value.length <= 3) {
            return `(${value.slice(0)}`;
        }

        if (value.length <= 6) {
            return `(${value.slice(0, 3)}) ${value.slice(3, 6)}`;
        }

        return `(${value.slice(0, 3)}) ${value.slice(3, 6)}-${value.slice(6, 10)}`;
    }

    unFormatValue(value: string): string {
        let n = value.replace(/\D/g, "");
        return n;
    }

    allowKey(key: string, newValue: string) {
        return this.newValueFilter.test(newValue);
    }
}
