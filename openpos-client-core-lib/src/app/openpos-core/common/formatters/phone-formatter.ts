import { IFormatter, IKeyFilter } from './iformatter';

export class PhoneFormatter implements IFormatter, IKeyFilter {
    static readonly US_FILTER_REGEX = [/[2-9]/, /[0-8]/, /[0-9]/, /[2-9]/, /[0-9]/, /[0-9]/, /[0-9]/, /[0-9]/, /[0-9]/, /[0-9]/];
    static readonly CA_FILTER_REGEX = [/[0-9]/, /[0-9]/, /[0-9]/, /[0-9]/, /[0-9]/, /[0-9]/, /[0-9]/, /[0-9]/, /[0-9]/, /[0-9]/];

    locale: string;
    keyFilter: IKeyFilter;
    // keyFilter = /[0-9\ ]/;
    constructor() {
        this.keyFilter = this;
    }

    formatValue(value: string): string {
        // this.keyFilter = this;

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
        let returnValue = value.replace(/\D/g, '');
        if (returnValue && this.filter(returnValue.slice(0, -1), returnValue.slice(-1))) {
            returnValue = value.slice(0, -1);
        }
        return returnValue;
        /*
        let n = value.replace(/\D/g, "");
        return n;
        */
    }

    filter(valueBefore: string, inputChar: string): boolean {
        const index = (valueBefore ? valueBefore.length : 0) + (inputChar ? inputChar.length : 0) - 1;
        let regExFilter: RegExp[];
        if (this.locale.toLowerCase().indexOf('ca') >= 0) {
            regExFilter = PhoneFormatter.CA_FILTER_REGEX;
        } else {
            regExFilter = PhoneFormatter.US_FILTER_REGEX;
        }

        if (index >= 0 && index < regExFilter.length) {
            const regEx = regExFilter[index];
            const match = regEx.test(inputChar);
            return ! match;
        }
        return false;
    }
}
