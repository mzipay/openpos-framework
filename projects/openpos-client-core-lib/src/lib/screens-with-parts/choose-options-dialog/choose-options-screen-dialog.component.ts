import { Component } from '@angular/core';
import { DialogComponent } from '../../shared/decorators/dialog-component.decorator';
import { PosScreen } from '../../screens-deprecated/pos-screen/pos-screen.component';
import { ChooseOptionsScreenDialogInterface } from './choose-options-screen-dialog.interface';

@DialogComponent({
    name: 'ChooseOptions'
})
@Component({
  selector: 'app-choose-options-screen-dialog',
  templateUrl: './choose-options-screen-dialog.component.html',
  styleUrls: ['./choose-options-screen-dialog.component.scss']
})
export class ChooseOptionsScreenDialogComponent extends PosScreen<ChooseOptionsScreenDialogInterface> {

  constructor() {
      super();
   }

  buildScreen() { }
}
