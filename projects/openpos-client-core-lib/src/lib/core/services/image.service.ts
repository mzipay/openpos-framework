import { Injectable } from '@angular/core';
import { PersonalizationService } from './personalization.service';

@Injectable({
    providedIn: 'root',
})
export class ImageService {
    private token = '${apiServerBaseUrl}';

    constructor(private personalizationService: PersonalizationService) { }

    replaceImageUrl(originalUrl: string): string {
        const apiServerBaseUrl = this.personalizationService.getApiServerBaseURL();
        const url = originalUrl.replace(this.token, apiServerBaseUrl);

        return url;
    }

}


