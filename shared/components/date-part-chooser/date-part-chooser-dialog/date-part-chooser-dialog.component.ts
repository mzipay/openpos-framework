import { IDatePartChooserField } from '../../../../core/interfaces';
import { Input, Component, OnInit, Output, Inject } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material";

@Component({
    selector: 'app-date-part-chooser-dialog',
    templateUrl: './date-part-chooser-dialog.component.html',
    styleUrls: ['./date-part-chooser-dialog.component.scss']
})

export class DatePartChooserDialogComponent implements OnInit {

    @Input() title: string;
    @Input() closeButton: boolean;
    @Input() dateParts: IDatePartChooserField;

    constructor(@Inject(MAT_DIALOG_DATA) public data: any,
        public dialogRef: MatDialogRef<DatePartChooserDialogComponent>) {
        if (data) {
            if (data.dateParts) {
                this.dateParts = <IDatePartChooserField> data.dateParts;
            }
            if (data.title) {
                this.title = data.title;
            }
            if (! data.disableClose) {
                this.closeButton = true;
            }
        }

    }

    ngOnInit(): void {
    }

}
