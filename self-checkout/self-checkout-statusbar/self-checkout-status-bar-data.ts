import { IScan } from '../../screens/templates/sell-template/sell/isell-template';
import { IMenuItem } from '../../common/imenuitem';

export class SelfCheckoutStatusBarData {
    showScan: boolean;
    backButton: IMenuItem;
    showSkip: boolean;
    showAdmin: boolean;
    scanSomethingData: IScan;
}
