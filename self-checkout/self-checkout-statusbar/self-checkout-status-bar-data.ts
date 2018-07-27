import { IScan } from '../../screens';
import { IMenuItem } from '../../core';

export class SelfCheckoutStatusBarData {
    showScan: boolean;
    backButton: IMenuItem;
    showSkip: boolean;
    showAdmin: boolean;
    scanSomethingData: IScan;
}
