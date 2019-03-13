import { Component } from '@angular/core';
import { SelectionListComponent } from './selection-list.component';
import { DialogComponent } from '../../shared/decorators/dialog.decorator';

@DialogComponent({
    name: 'SelectionListDialog',
    moduleName: 'Core'
})
@Component({
    selector: 'app-selection-list-dialog',
    templateUrl: './selection-list-dialog.component.html',
    styleUrls: ['./selection-list.component.scss']
})
export class SelectionListDialogComponent extends SelectionListComponent {
}
