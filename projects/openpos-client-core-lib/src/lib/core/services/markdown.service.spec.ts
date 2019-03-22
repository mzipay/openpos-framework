import { Injectable } from '@angular/core';
import { MarkdownService } from './markdown.service';
import { iterateListLike } from '@angular/core/src/change_detection/change_detection_util';
import { MatExpansionPanelContent } from '@angular/material';

describe('MarkdownService', () => {

    let markdownService: MarkdownService;

    beforeEach(() => {
        markdownService = new MarkdownService();
    });

    it('test simple transform of text to bold', () => {
        expect(markdownService.transform('**bold text**')).toBe('<p><strong>bold text</strong></p>');
    });

    it('test centering of text', () => {
        expect(markdownService.transform('>>center me<<')).toBe('<div class="text-center">center me</div>');
    });

    it('test large text', () => {
        expect(markdownService.transform('^^make text large^^')).toBe('<p><span class="text-lg">make text large</span></p>');
    });

    it('test warn text', () => {
        expect(markdownService.transform('&&Wwarning&&')).toBe('<p><span class="warn">warning</span></p>');
        expect(markdownService.transform('&&wwarning&&')).toBe('<p><span class="warn">warning</span></p>');
    });

    it('test primary color text', () => {
        expect(markdownService.transform('&&Ptext in primary color&&')).toBe('<p><span class="primary">text in primary color</span></p>');
        expect(markdownService.transform('&&ptext in primary color&&')).toBe('<p><span class="primary">text in primary color</span></p>');
    });

    it('test accent color text', () => {
        expect(markdownService.transform('&&Atext in accent color&&')).toBe('<p><span class="accent">text in accent color</span></p>');
        expect(markdownService.transform('&&atext in accent color&&')).toBe('<p><span class="accent">text in accent color</span></p>');
    });

    it('test large warn centered text', () => {
        expect(markdownService.transform('>>^^&&wlarge centered warn text&&^^<<')).toBe('<div class="text-center"><span class="text-lg"><span class="warn">large centered warn text</span></span></div>');
    });

    it('test large italic text', () => {
        expect(markdownService.transform('^^*large italic text*^^')).toBe('<p><span class="text-lg"><em>large italic text</em></span></p>');
    });

});
