import { Injectable } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { PersonalizationService } from './personalization.service';

@Injectable({
    providedIn: 'root',
})
export class ImageService {
    private token = '${apiServerBaseUrl}';

    constructor(private personalizationService: PersonalizationService, private sanitizer: DomSanitizer) { }

    replaceImageUrl(originalUrl: string): string {
        const apiServerBaseUrl = this.personalizationService.getApiServerBaseURL();
        const url = originalUrl.replace(this.token, apiServerBaseUrl);

        return url;
        // return this.sanitizer.bypassSecurityTrustUrl(url);
    }

}


