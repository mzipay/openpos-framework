import { IScan } from '../../screens';
import { IActionItem } from '../../core';

export class SelfCheckoutStatusBarData {
    showScan: boolean;
    backButton: IActionItem;
    showSkip: boolean;
    showAdmin: boolean;
    scanSomethingData: IScan;
}
