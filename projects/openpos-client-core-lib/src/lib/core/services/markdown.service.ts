import { Injectable } from '@angular/core';
import { Converter, RegexReplaceExtension, FilterExtension, ConverterOptions } from 'showdown';

/**
 * Currently supported custom markdown:
 *
 * `>>some text<<`: center text <br/>
 *
 * `^^some text^^`: large text (uses `text-lg` css class)
 *
 * `&&wsome text&&` or  `&&Wsome text&&`: color text with warning color (uses `warn` css class)
 *
 * `&&asome text&&` or  `&&Asome text&&`: color text with accent color (uses `accent` css class)
 *
 * `&&psome text&&` or  `&&Psome text&&`: color text with primary color (uses `primary` css class)
 */
@Injectable({
    providedIn: 'root',
})
export class MarkdownService {

    protected converter: Converter;

    constructor() {
        const centerExt: FilterExtension = {
            type: 'lang',
            filter: (text: string, converter: Converter, options?: ConverterOptions) => {
                    return text.replace(/>>(.*)<</g, '<div class="text-center">$1</div>');
            }
        };
        const largeTextExt: FilterExtension = {
            type: 'lang',
            filter: (text: string, converter: Converter, options?: ConverterOptions) => {
                    return text.replace(/\^\^(.*)\^\^/g, '<span class="text-lg">$1</span>');
            }
        };
        const warnExt: FilterExtension = {
            type: 'lang',
            filter: (text: string, converter: Converter, options?: ConverterOptions) => {
                    return text.replace(/&&[Ww](.*)&&/g, '<span class="warn">$1</span>');
                }
        };
        const primaryColorExt: FilterExtension = {
            type: 'lang',
            filter: (text: string, converter: Converter, options?: ConverterOptions) => {
                    return text.replace(/&&[Pp](.*)&&/g, '<span class="primary">$1</span>');
                }
        };
        const accentColorExt: FilterExtension = {
            type: 'lang',
            filter: (text: string, converter: Converter, options?: ConverterOptions) => {
                    return text.replace(/&&[Aa](.*)&&/g, '<span class="accent">$1</span>');
            }
        };

        this.converter = new Converter();
        this.converter.addExtension(centerExt, 'centerExt');
        this.converter.addExtension(warnExt, 'warnExt');
        this.converter.addExtension(largeTextExt, 'largeTextExt');
        this.converter.addExtension(primaryColorExt, 'primaryColorExt');
        this.converter.addExtension(accentColorExt, 'accentColorExt');
    }


    transform(markdownText: string): string {
        return this.converter.makeHtml(markdownText);
    }
}
