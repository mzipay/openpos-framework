
import { Component } from '@angular/core';
import { ChooseOptionsComponent } from './choose-options.component';
import { DialogComponent } from '../../shared/decorators/dialog-component.decorator';
import { OpenPOSDialogConfig } from '../../core/interfaces/open-pos-dialog-config.interface';

@DialogComponent({
    name: 'ChooseOptions'
})
@Component({
  selector: 'app-choose-options-dialog',
  templateUrl: './choose-options-dialog.component.html',
  styleUrls: ['./choose-options-dialog.component.scss']
})
export class ChooseOptionsDialogComponent extends ChooseOptionsComponent {

    dialogProperties: OpenPOSDialogConfig;

    show(screen: any) {
        super.show(screen);
        if (screen.dialogProperties) {
            this.dialogProperties = screen.dialogProperties;
        }
    }
}
