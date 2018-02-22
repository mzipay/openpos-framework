import { IFormatter } from './iformatter';

export class NumericFormatter implements IFormatter {
    static readonly FILTER_REGEX = /^[0-9]$/;


    allowKey(key: string, newValue: string): boolean {
        return NumericFormatter.FILTER_REGEX.test(key);
    }

    formatValue(value: string): string {
        return value;
    }

    unFormatValue(value: string): string {
        return value;
    }
}
