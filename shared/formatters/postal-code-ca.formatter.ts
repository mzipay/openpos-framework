import { IFormatter } from './formatter.interface';

export class PostalCodeCAFormatter implements IFormatter {
    static readonly CHAR_REGEX = /[^0-9a-zA-Z \-]/g;
    static readonly FILTER_REGEXS = [
        /[abceghjklmnprstvxyABCEGHJKLMNPRSTVXY]/,
        /[0-9]/,
        /[abceghjklmnprstvwxyzABCEGHJKLMNPRSTVWXYZ]/,
        / /,
        /[0-9]/,
        /[abceghjklmnprstvwxyzABCEGHJKLMNPRSTVWXYZ]/,
        /[0-9]/
    ];

    allowKey(key: string, newValue: string): boolean {
        const len = newValue.length;
        if (len > 0 && len <= PostalCodeCAFormatter.FILTER_REGEXS.length) {
            return PostalCodeCAFormatter.FILTER_REGEXS[len - 1].test(newValue[len - 1]);
        }
        return false;
    }

    formatValue(value: string): string {
        // remove any invalid chars
        let returnValue = value.replace(PostalCodeCAFormatter.CHAR_REGEX, '');
        // trim to max len of 20
        if (returnValue && returnValue.length > 20) {
            returnValue = returnValue.substring(0, 20);
        }
        return returnValue;
    }

    unFormatValue(value: string): string {
        return value;
    }
}
