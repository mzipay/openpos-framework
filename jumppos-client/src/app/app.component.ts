import { LoaderComponent } from './common/loader/loader.component';
import { Component } from '@angular/core';
import { SessionService } from './session.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {

  constructor(public session: SessionService) {
  }

}
