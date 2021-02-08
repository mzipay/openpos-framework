import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { PeripheralSelectionService, PeripheralCategory, PeripheralDevice } from '../../../../core/peripherals/peripheral-selection.service';

export interface PeripheralSelectorDialogData {
    category: PeripheralCategory
}
@Component({
    templateUrl: './peripheral-selector.component.html',
    styleUrls: ['./peripheral-selector.component.scss']
})
export class PeripheralSelectorComponent {

    public selectedDevice: PeripheralDevice;
    
    constructor(
        @Inject(MAT_DIALOG_DATA) public data: PeripheralSelectorDialogData,
        private dialogRef: MatDialogRef<PeripheralSelectorComponent>,
        public periph: PeripheralSelectionService
    ) {
        this.selectedDevice = this.data.category.selectedDevice
    }

    assignDevice() {
        this.periph.selectDevice(this.data.category, this.selectedDevice);
        this.dialogRef.close();
    }

    close() {
        this.dialogRef.close();
    }
}
