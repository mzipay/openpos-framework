import { IDatePartChooserField } from './../../../core/interfaces';
import { Input, Component, OnInit, Output, Inject } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material";
import { DatePartChooserMode } from '../../../core/interfaces';
import { Title } from '@angular/platform-browser';

@Component({
    selector: 'app-date-part-chooser',
    templateUrl: './date-part-chooser.component.html',
    styleUrls: ['./date-part-chooser.component.scss']
})

export class DatePartChooserComponent implements OnInit {

    /**
     * Array of month abbreviations.  1-based, where month at index 1 = January.
     */
    static readonly MONTHS: string[] =
        [null, 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    static readonly MONTH_DAYS: number[] =
        [null, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

    _dateChooserModeType = DatePartChooserMode;

    // @Input() formGroup: FormGroup;
    // @Input() controlName: string;
    // @Input() required: boolean;
    // @Input() placeholder: string;
    // @Input() label: string;

    @Input() title: string;
    @Input() mode: DatePartChooserMode = DatePartChooserMode.MonthDate;
    @Output() @Input() month: number;
    @Input() dayOfMonth: number;
    @Input() year: number;

    constructor(@Inject(MAT_DIALOG_DATA) public data: any,
        public dialogRef: MatDialogRef<DatePartChooserComponent>) {
        if (data) {
            if (data.dateParts) {
                const dateParts = <IDatePartChooserField> data.dateParts;
                this.month = dateParts.month;
                this.dayOfMonth = dateParts.dayOfMonth;
                this.year = dateParts.year;
                this.mode = dateParts.mode;
            }
            if (data.title) {
                this.title = data.title;
            }
        }

    }

    ngOnInit(): void {
    }

    get months() {
        return DatePartChooserComponent.MONTHS;
    }

    lastWeekMonthDays() {
        return Array.from({length: (this.monthDays - 28)}, (v, k) => k + 29);
    }

    get monthDays() {
        if (this.month && this.month >= 1 && this.month <= 12) {
            return DatePartChooserComponent.MONTH_DAYS[this.month];
        } else {
            return 31;
        }
    }
}
/*
export enum DatePartChooserMode {
    MonthDate = 'MonthDate',
    MonthYear = 'MonthYear',
    MonthDateYear = 'MonthDateYear'
}
*/