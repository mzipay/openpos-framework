
import { Component } from '@angular/core';
import { ChooseOptionsComponent } from './choose-options.component';
import { OpenPOSDialogConfig } from './../../core';


@Component({
  selector: 'app-choose-options-dialog',
  templateUrl: './choose-options-dialog.component.html',
  styleUrls: ['./choose-options-dialog.component.scss']
})
export class ChooseOptionsDialogComponent extends ChooseOptionsComponent {

    dialogProperties: OpenPOSDialogConfig;

    show(screen: any) {
        super.show(screen);
        if (screen.template && screen.template.dialogProperties) {
            this.dialogProperties = screen.template.dialogProperties;
        }
    }
    
  
}
