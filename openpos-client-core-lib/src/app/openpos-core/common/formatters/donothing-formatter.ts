import { IFormatter } from "./iformatter";

export class DoNothingFormatter implements IFormatter {
   
    formatValue(value: string): string {
        if( !value ) return "";
        return value;
    }
   
    unFormatValue(value: string): string {
        return value;
    }

    keyFilter = null;
}