import { OpenPOSDialogConfig } from '../../core/interfaces/open-pos-dialog-config.interface';
import { SystemStatusComponent } from './system-status.component';
import { Component } from '@angular/core';

@Component({
    selector: 'app-system-status-dialog',
    templateUrl: './system-status-dialog.component.html',
    styleUrls: ['./system-status.component.scss']
  })
  export class SystemStatusDialogComponent extends SystemStatusComponent {

      dialogProperties: OpenPOSDialogConfig;

      show(screen: any) {
          super.show(screen);
          if (screen.template && screen.template.dialogProperties) {
              this.dialogProperties = screen.template.dialogProperties;
          }
      }
  }
