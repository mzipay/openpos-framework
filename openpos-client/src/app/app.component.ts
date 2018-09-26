import { Component } from '@angular/core';
import { PluginService, SessionService, PersonalizationService } from '@jumpmind/openpos-client-core-lib';
@Component({
    selector: 'app-root',
    templateUrl: './app.component.html'
})
export class AppComponent {

    constructor(protected pluginService: PluginService, protected personalization: PersonalizationService) {
        personalization.setTheme('default-theme', true);
    }

}
