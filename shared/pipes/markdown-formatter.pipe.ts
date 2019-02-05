import { Pipe, PipeTransform } from '@angular/core';
import { MarkdownService } from 'ngx-markdown';

@Pipe({ name: 'markdownFormatter' })
export class MarkdownFormatterPipe implements PipeTransform {

    constructor( private markdownService: MarkdownService ) {}

    transform(value: string): string {
        return this.markdownService.compile(value);
    }
}
