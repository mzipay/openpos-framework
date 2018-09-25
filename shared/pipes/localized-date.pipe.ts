import { Pipe, PipeTransform } from '@angular/core';
import { FormattersService } from '../../core';

@Pipe({ name: 'localizeddatetime' })
export class LocalizedDatePipe implements PipeTransform {

    constructor(private formatters: FormattersService) {
    }

    transform(value: any) {
        return this.formatters.getFormatter('datetime').formatValue(value);
    }

}
