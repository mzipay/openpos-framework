import { IFormatter } from "./iformatter";

export class MoneyFormatter implements IFormatter {
   
    locale?: string;

    formatValue(value: string): string {
        if( !value ) return "";

        let parts = value.split('.');
        
        if( parts.length > 1 ){
            return `$${parts[0]}.${parts[1].slice(0,2)}`;
        }
        
        return `$${parts[0]}.00`;
    }
   
    unFormatValue(value: string): string {
        let n = value.replace(/[^(\d|\.)]/g, "");
        return n;
    }

    allowKey(key: string, newValue: string): boolean {
        return this.keyFilter.test(key);
    }

     private keyFilter = /[0-9\ | \.]/;
}