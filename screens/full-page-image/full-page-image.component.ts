import { Component } from '@angular/core';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
  selector: 'app-full-page-image',
  templateUrl: './full-page-image.component.html'
})
export class FullPageImageComponent extends PosScreen<any> {
  constructor() {
      super();
  }

  buildScreen(){}

}
