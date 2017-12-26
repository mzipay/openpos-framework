import { DeviceService } from './services/device.service';
import { LoaderComponent } from './common/loader/loader.component';
import { Component } from '@angular/core';
import { SessionService } from './services/session.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {

  constructor(public session: SessionService, public deviceService: DeviceService) {
  }

}
