import { Component } from '@angular/core';
import { PosScreen } from '../pos-screen/pos-screen.component';

/**
 * @ignore
 */
@Component({
  selector: 'app-dynamic-list',
  templateUrl: './dynamic-list.component.html'
})
export class DynamicListComponent extends PosScreen<any> {

  buildScreen() {}

}
