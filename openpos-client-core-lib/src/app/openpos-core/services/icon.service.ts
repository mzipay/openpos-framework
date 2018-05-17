import { SessionService } from './session.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Injectable } from '@angular/core';
import { MatIconRegistry } from '@angular/material';

@Injectable()
export class IconService {
    constructor(private iconRegistry: MatIconRegistry,
        private sanitizer: DomSanitizer, private sessionService: SessionService) {
        this.iconRegistry.addSvgIcon('local_calculator', this.makeIconSafeUrl('calculator.svg'));
        this.iconRegistry.addSvgIcon('local_cash', this.makeIconSafeUrl('cash.svg'));
        this.iconRegistry.addSvgIcon('local_cash-multiple', this.makeIconSafeUrl('cash-multiple.svg'));
        this.iconRegistry.addSvgIcon('local_postvoid', this.makeIconSafeUrl('postvoid.svg'));
        this.iconRegistry.addSvgIcon('barcode', this.makeIconSafeUrl('barcode.svg'));
        this.iconRegistry.addSvgIcon('local_barcode', this.makeIconSafeUrl('barcode.svg'));
        this.iconRegistry.addSvgIcon('percent', this.makeIconSafeUrl('percent.svg'));
        this.iconRegistry.addSvgIcon('local_rotate-3d', this.makeIconSafeUrl('rotate-3d.svg'));
    }

    public addIcon(key: string, asset: string, served?: boolean) {
        if (!served) {
            // TODO: needs fixed. adding icons as local file resources will not work when running the application
            // as standalone javascript app in Cordova or locally in file system.  (i.e., when not served)
            this.iconRegistry.addSvgIcon(key, this.sanitizer.bypassSecurityTrustResourceUrl(`./assets/icons/${asset}`));
        } else {
            this.iconRegistry.addSvgIcon(key, this.makeIconSafeUrl(asset));
        }
    }

    private makeIconSafeUrl(iconFilename: string): SafeResourceUrl {
        return this.sanitizer.bypassSecurityTrustResourceUrl(`${this.sessionService.getServerBaseURL()}/img/${iconFilename}`);
    }
}
