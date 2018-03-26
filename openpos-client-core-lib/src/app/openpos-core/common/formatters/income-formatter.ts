import { IFormatter } from './iformatter';

export class IncomeFormatter implements IFormatter {
    static readonly FILTER_REGEX = /^[0-9]$/;


    allowKey(key: string, newValue: string): boolean {
        return IncomeFormatter.FILTER_REGEX.test(key);
    }

    formatValue(value: string): string {

        return value;
        /*
        let formattedValue = '';
        let strippedValue = value.replace(',', '');
        if (value.length <= 3) {
            strippedValue = strippedValue + '000';
        }

        let count = value.length;
        for (let idx = strippedValue.length - 1; idx >= 0; idx--) {
            if (count % 4 === 0 && strippedValue.charAt(idx) !== ',') {
                formattedValue = ',' + formattedValue;
            }
            formattedValue = strippedValue.charAt(idx) + formattedValue;
            count--;
        }

        return formattedValue;
        */
    }

    unFormatValue(value: string): string {
        return value;
    }
}
