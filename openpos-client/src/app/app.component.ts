import { Component } from '@angular/core';
import { PluginService } from 'openpos-core/services';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {

  constructor(public pluginService: PluginService) {
  }

}
