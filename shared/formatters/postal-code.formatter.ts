import { IFormatter } from './formatter.interface';

export class PostalCodeFormatter implements IFormatter {
    static readonly FILTER_REGEX = /^[0-9a-zA-Z\-]{1,20}$/;

    allowKey(key: string, newValue: string): boolean {
        return PostalCodeFormatter.FILTER_REGEX.test(newValue);
    }

    formatValue(value: string): string {
        return value;
    }

    unFormatValue(value: string): string {
        return value;
    }
}
