import { IFormElement } from '.';

export enum DatePartChooserMode {
    MonthDate = 'MonthDate',
    MonthYear = 'MonthYear',
    MonthDateYear = 'MonthDateYear'
}

export interface IDatePartChooserField  extends IFormElement {
    month: number;
    dayOfMonth: number;
    year: number;
    mode: DatePartChooserMode;
}

