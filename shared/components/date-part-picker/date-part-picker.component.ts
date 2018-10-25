import { Input, Component, OnInit, Output } from "@angular/core";

@Component({
    selector: 'app-date-part-picker',
    templateUrl: './date-part-picker.component.html',
    styleUrls: ['./date-part-picker.component.scss']
})

export class DatePartPickerComponent implements OnInit {

    /**
     * Array of month abbreviations.  1-based, where month at index 1 = January.
     */
    static readonly MONTHS: string[] =
        [null, 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    static readonly MONTH_DAYS: number[] =
        [null, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

    _datePickerModeType = DatePartPickerMode;

    @Input() mode: DatePartPickerMode = DatePartPickerMode.MonthDate;
    @Output() @Input() month: number;
    @Input() dayOfMonth: number;
    @Input() year: number;


    ngOnInit(): void {
    }

    get months() {
        return DatePartPickerComponent.MONTHS;
    }

    lastWeekMonthDays() {
        return Array.from({length: (this.monthDays - 28)}, (v, k) => k + 29);
    }

    get monthDays() {
        if (this.month && this.month >= 1 && this.month <= 12) {
            return DatePartPickerComponent.MONTH_DAYS[this.month];
        } else {
            return 31;
        }
    }
}

export enum DatePartPickerMode {
    MonthDate = 'MonthDate',
    MonthYear = 'MonthYear',
    MonthDateYear = 'MonthDateYear'
}
