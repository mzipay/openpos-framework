import { IFormatter } from "./iformatter";

export class PhoneFormatter implements IFormatter {
   
    formatValue(value: string): string {
        if( !value ) return "";

        if( value.length <= 3 ){
          return `(${value.slice(0)}`;
        }
    
        if( value.length <= 6 ){
          return `(${value.slice(0,3)}) ${value.slice(3,6)}`;
        }
    
        return `(${value.slice(0,3)}) ${value.slice(3,6)}-${value.slice(6,10)}`;
    }
   
    unFormatValue(value: string): string {
        let n = value.replace(/\D/g, "");
        return n;
    }

    keyFilter = /[0-9\ ]/;
}