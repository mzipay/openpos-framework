import { DomSanitizer } from '@angular/platform-browser';
import { Injectable } from '@angular/core';
import { MdIconRegistry } from '@angular/material';

@Injectable()
export class IconService {
    constructor( private iconRegistry: MdIconRegistry,
        private sanitizer: DomSanitizer) {
    }

    /**
     * Registers SVG icon resources that are available locally to this app so that the md-icon directive can
     * access them.
     */
    registerLocalSvgIcons(): void {
        this.iconRegistry.addSvgIcon('local_calculator', this.sanitizer.bypassSecurityTrustResourceUrl('/assets/calculator.svg'));
        this.iconRegistry.addSvgIcon('local_cash', this.sanitizer.bypassSecurityTrustResourceUrl('/assets/cash.svg'));
        this.iconRegistry.addSvgIcon('local_cash-multiple', this.sanitizer.bypassSecurityTrustResourceUrl('/assets/cash-multiple.svg'));
    }
}
