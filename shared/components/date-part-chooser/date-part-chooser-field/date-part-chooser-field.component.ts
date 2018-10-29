import { DatePartChooserComponent } from '../date-part-chooser.component';
import { Input, Component, OnInit, Output, EventEmitter, AfterViewInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { IDatePartChooserField } from '../../../../core';
import { FormattersService } from '../../../../core';
import { MatDialog } from '@angular/material';

@Component({
    selector: 'app-date-part-chooser-field',
    templateUrl: './date-part-chooser-field.component.html',
    styleUrls: ['./date-part-chooser-field.component.scss']
})

export class DatePartChooserFieldComponent implements OnInit, AfterViewInit {

    /*
     * Array of month abbreviations.  1-based, where month at index 1 = January.
    static readonly MONTHS: string[] =
        [null, 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    static readonly MONTH_DAYS: number[] =
        [null, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
     */

    // _datePickerModeType = DatePartPickerMode;

    @Input() formGroup: FormGroup;
    @Input() controlName: string;
    @Input() required: boolean;
    @Input() placeholder: string;
    @Input() label: string;
    @Input() iconName: string;
    @Input() formatterName: string;
    @Input() deleteAllowed = true;
    @Output() delete = new EventEmitter<IDatePartChooserField>();
    @Input() deleteIcon = 'delete_forever';
    @Output() @Input() model: IDatePartChooserField;

    // @Input() mode: DatePartPickerMode = DatePartPickerMode.MonthDate;

    @Output() valueChange = new EventEmitter<any>();

    constructor(protected formatters: FormattersService,
        protected dialog: MatDialog,
    ) {

    }

    ngAfterViewInit(): void {
    }

    ngOnInit(): void {
        if (this.formGroup) {
            this.setFieldValue(this.formatForDisplay());
        }
    }

    private isLeapYear(year: number): boolean {
        return ((year % 4 === 0) && (year % 100 !== 0)) || (year % 400 === 0);
    }

    formatForDisplay(): string {
        const d: Date = new Date();
        d.setDate(1);  // Default to first of the month
        if (this.model.month) {
            d.setMonth(this.model.month - 1);
        }
        if (this.model.dayOfMonth) {
            if (this.model.month === 2 && this.model.dayOfMonth === 29) {
                // adjust year to nearest leap year
                let year = d.getFullYear();
                while (! this.isLeapYear(year) && year > 0) {
                    year--;
                }
                d.setFullYear(year);
            }
            d.setDate(this.model.dayOfMonth);
        }
        if (this.model.year) {
            d.setFullYear(this.model.year);
        }

        return this.formatters.getFormatter(this.formatterName).formatValue(d);
    }

    private setFieldValue(value: string) {
        const patchGroup = {};
        patchGroup[this.controlName] = value;

        setTimeout(() => this.formGroup.patchValue(patchGroup), 0);
    }

    onDelete() {
        if (this.deleteAllowed) {
            this.setFieldValue(null);
        }
        this.delete.emit(this.model);
    }

    openDatePartChooser() {
        const dialogRef = this.dialog.open(DatePartChooserComponent, {
            width: '70%',
            data: {
              dateParts: this.model,
              disableClose: false,
              autoFocus: false
           }
        });

    }
    /*
      openPopTart() {
    const dialogRef = this.dialog.open(PopTartComponent, {
      width: '70%',
      data: {
        optionItems: this.values,
        disableClose: false,
        autoFocus: false
     }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.log.info('pop tart closed with value of: ' + result);
      this.formGroup.get(this.formField.id).setValue(result);
      this.onFormElementChanged(this.formField);
    });
  }
*/
    /*
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
    */
}

/*
export enum DatePartPickerMode {
    MonthDate = 'MonthDate',
    MonthYear = 'MonthYear',
    MonthDateYear = 'MonthDateYear'
}
*/