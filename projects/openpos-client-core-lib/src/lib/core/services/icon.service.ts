import { Logger } from './logger.service';
import { SessionService } from './session.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Injectable } from '@angular/core';
import { MatIconRegistry } from '@angular/material';
import { PersonalizationService } from './personalization.service';

@Injectable({
    providedIn: 'root',
  })
export class IconService {
    private icons = new Map<string, IconDefinition>();

    constructor(private log: Logger, private iconRegistry: MatIconRegistry,
        private sanitizer: DomSanitizer, private personalization: PersonalizationService, private sessionService: SessionService) {
        this.sessionService.onServerConnect.subscribe(connected => {
            if (connected) {
                this.init();
            }
        });
    }

    private init() {
        // This is no longer used. IconConstants should be used to register icons from here on out
        this.log.info(`Icon service is initializing using base server url: ${this.personalization.getServerBaseURL()}`);
        this.iconRegistry.addSvgIcon('openpos_calculator', this.makeIconSafeUrl('calculator.svg'));
        this.iconRegistry.addSvgIcon('openpos_cash', this.makeIconSafeUrl('cash.svg'));
        this.iconRegistry.addSvgIcon('openpos_cash-multiple', this.makeIconSafeUrl('cash-multiple.svg'));
        this.iconRegistry.addSvgIcon('openpos_postvoid', this.makeIconSafeUrl('postvoid.svg'));
        this.iconRegistry.addSvgIcon('barcode', this.makeIconSafeUrl('barcode.svg'));
        this.iconRegistry.addSvgIcon('openpos_barcode', this.makeIconSafeUrl('barcode.svg'));
        this.iconRegistry.addSvgIcon('percent', this.makeIconSafeUrl('percent.svg'));
        this.iconRegistry.addSvgIcon('openpos_percent', this.makeIconSafeUrl('percent.svg'));
        this.iconRegistry.addSvgIcon('openpos_rotate-3d', this.makeIconSafeUrl('rotate-3d.svg'));
        this.iconRegistry.addSvgIcon('openpos_book-open-page-variant', this.makeIconSafeUrl('book-open-page-variant.svg'));
    }

    public addIcon(name: string, iconDef: IconDefinition, served?: boolean) {
        if (this.icons.get(name)) {
            this.log.info(`replacing registration for icon of type ${this.icons.get(name).iconName} with ${iconDef.iconName} for the key of ${name} in the icon service`);
            this.icons.delete(name);
        }
        if (iconDef.iconType === 'svg') {
            if (!served) {
            // TODO: needs fixed. adding icons as local file resources will not work when running the application
            // as standalone javascript app in Cordova or locally in file system.  (i.e., when not served)
                this.iconRegistry.addSvgIcon(name, this.sanitizer.bypassSecurityTrustResourceUrl(`./assets/icons/${iconDef.iconName}`));
            } else {
                this.iconRegistry.addSvgIcon(name, this.makeIconSafeUrl(iconDef.iconName));
            }
        }
        this.icons.set(name, iconDef);
    }

    public resolveIcon(name: string): IconDefinition {
        if (this.icons.has(name)) {
            return this.icons.get(name);
        }
        return null;
    }

    private makeIconSafeUrl(iconFilename: string): SafeResourceUrl {
        return this.sanitizer.bypassSecurityTrustResourceUrl(`${this.personalization.getServerBaseURL()}/img/${iconFilename}`);
    }
}

export class IconDefinition {
    iconName: string;
    iconType: string;

    constructor(iconName: string, iconType: string) {
        this.iconName = iconName;
        this.iconType = iconType;
    }
}
