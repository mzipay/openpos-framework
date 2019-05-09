import { Component } from '@angular/core';
import { PosScreen } from '../pos-screen/pos-screen.component';
import { ScreenComponent } from '../../shared/decorators/screen-component.decorator';

/**
 * @ignore
 */
@ScreenComponent({
  name: 'DynamicForm'
})
@Component({
  selector: 'app-dynamic-form',
  templateUrl: './dynamic-form.component.html'
})
export class DynamicFormComponent extends PosScreen<any> {

  buildScreen() {}

}
