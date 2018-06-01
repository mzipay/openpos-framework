import { Component } from '@angular/core';
import { PluginService, SessionService } from '@jumpmind/openpos-client-core-lib';
@Component({
    selector: 'app-root',
    templateUrl: './app.component.html'
})
export class AppComponent {

    constructor(protected pluginService: PluginService, protected session: SessionService) {
        this.session.setTheme('default-theme');
    }

}
