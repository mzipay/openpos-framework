import { IActionItem } from '../../core/actions/action-item.interface';
import { IScan } from '../../shared/components/scan-something/scan.interface';


export class SelfCheckoutStatusBarData {
    showScan: boolean;
    backButton: IActionItem;
    skipButton: IActionItem;
    showAdmin: boolean;
    showLanguageSelector: boolean;
    scanSomethingData: IScan;
    type: any;
}
