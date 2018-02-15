import { Injectable } from '@angular/core';
import { IFormatter } from 'openpos-core/common/formatters/iformatter';
import { PhoneFormatter } from 'openpos-core/common/formatters/phone-formatter';


@Injectable()
export class FormattersService {
    private formatters = new Map<string, IFormatter>();

    constructor() {
        this.formatters.set("PHONE", new PhoneFormatter());
    }
    

    getFormatter( name: string): IFormatter{
        return this.formatters.get(name);
    }

 
}
