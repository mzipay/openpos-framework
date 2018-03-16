import { DomSanitizer } from '@angular/platform-browser';
import { Injectable } from '@angular/core';
import { MatIconRegistry } from '@angular/material';

@Injectable()
export class IconService {
    constructor(private iconRegistry: MatIconRegistry,
        private sanitizer: DomSanitizer) {
        this.iconRegistry.addSvgIcon('local_calculator', this.sanitizer.bypassSecurityTrustResourceUrl('./assets/icons/calculator.svg'));
        this.iconRegistry.addSvgIcon('local_cash', this.sanitizer.bypassSecurityTrustResourceUrl('./assets/icons/cash.svg'));
        this.iconRegistry.addSvgIcon('local_cash-multiple',
            this.sanitizer.bypassSecurityTrustResourceUrl('./assets/icons/cash-multiple.svg'));
        this.iconRegistry.addSvgIcon('local_postvoid', this.sanitizer.bypassSecurityTrustResourceUrl('./assets/icons/postvoid.svg'));
        this.iconRegistry.addSvgIcon('barcode', this.sanitizer.bypassSecurityTrustResourceUrl('./assets/icons/barcode.svg'));
        this.iconRegistry.addSvgIcon('percent', this.sanitizer.bypassSecurityTrustResourceUrl('./assets/icons/percent.svg'));
    }

    public addIcon(key: string, asset: string) {
        this.iconRegistry.addSvgIcon(key, this.sanitizer.bypassSecurityTrustResourceUrl(`./assets/icons/${asset}`));
    }
}
