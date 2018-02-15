import { Injectable } from '@angular/core';
import { IFormatter } from '../common/formatters/iformatter';
import { PhoneFormatter } from '../common/formatters/phone-formatter';
import { DoNothingFormatter } from '../common/formatters/donothing-formatter';


@Injectable()
export class FormattersService {
    private formatters = new Map<string, IFormatter>();

    constructor() {
        this.formatters.set("phoneus", new PhoneFormatter());
    }
    

    getFormatter( name: string): IFormatter{
        let lname = name.toLowerCase();
        if(this.formatters.has(lname)){
            return this.formatters.get(lname);
        }

        return new DoNothingFormatter();
    }

 
}
