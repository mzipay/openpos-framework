import { Component } from '@angular/core';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
  selector: 'app-options',
  templateUrl: './options.component.html',
  styleUrls: ['./options.component.scss']
})
export class OptionsComponent extends PosScreen<any> {

  buildScreen() {}

}
