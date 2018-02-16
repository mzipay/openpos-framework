import { Injectable } from '@angular/core';
import { IFormatter } from '../common/formatters/iformatter';
import { PhoneFormatter } from '../common/formatters/phone-formatter';
import { DoNothingFormatter } from '../common/formatters/donothing-formatter';
import { MoneyFormatter } from '../common/formatters/money-formatter';


@Injectable()
export class FormattersService {
    private formatters = new Map<string, IFormatter>();

    constructor() {
        this.formatters.set("phone", new PhoneFormatter());
        this.formatters.set("money", new MoneyFormatter())
    }
    

    getFormatter( name: string): IFormatter{
        if( name ){
            let lname = name.toLowerCase();
            if(this.formatters.has(lname)){
                return this.formatters.get(lname);
            }
        }

        return new DoNothingFormatter();
    }

 
}
