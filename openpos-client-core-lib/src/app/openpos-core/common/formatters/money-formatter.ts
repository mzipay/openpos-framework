import { IFormatter } from "./iformatter";

export class MoneyFormatter implements IFormatter {
   
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

    keyFilter = /[0-9\ | \.]/;
}