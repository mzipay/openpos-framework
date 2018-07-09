import { IScan } from '../../screens/templates/sell-template/sell/sell-template.interface';
import { IMenuItem } from '../../core';

export class SelfCheckoutStatusBarData {
    showScan: boolean;
    backButton: IMenuItem;
    showSkip: boolean;
    showAdmin: boolean;
    scanSomethingData: IScan;
}
