import { Component } from '@angular/core';
import { MultipleFormDialogInterface } from './multiple-form-dialog.interface';
import { DialogComponent } from '../../shared/decorators/dialog-component.decorator';
import { PosScreen } from '../../screens-deprecated/pos-screen/pos-screen.component';

@DialogComponent({
    name: 'MultipleForm'
})
@Component({
  selector: 'app-multiple-form-dialog',
  templateUrl: './multiple-form-dialog.component.html',
  styleUrls: ['./multiple-form-dialog.component.scss']
})
export class MultipleFormDialogComponent extends PosScreen<MultipleFormDialogInterface> {

  constructor() {
      super();
   }

  buildScreen() { }
}
