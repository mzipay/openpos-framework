import { IFormatter } from './iformatter';

export class PercentageFormatter implements IFormatter {
    static readonly FILTER_REGEX = /^100$|^\d{0,2}(\.\d{1,2})?$|^\d{0,2}(\.)?$/;

    allowKey(key: string, newValue: string): boolean {
        const match = PercentageFormatter.FILTER_REGEX.test(newValue);
        return match;
    }

    formatValue(value: string): string {
        const parts = value.split('.');

        if (parts.length > 1) {
            return `${parts[0]}.${parts[1].slice(0, 2)}`;
        }
        return `${parts[0]}`;
    }

    unFormatValue(value: string): string {
        return value;
    }
}
