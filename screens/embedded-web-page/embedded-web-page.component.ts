import { Component } from '@angular/core';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
  selector: 'app-embedded-web-page',
  templateUrl: './embedded-web-page.component.html'
})
export class EmbeddedWebPageComponent extends PosScreen<any> {

  buildScreen() {}
}
