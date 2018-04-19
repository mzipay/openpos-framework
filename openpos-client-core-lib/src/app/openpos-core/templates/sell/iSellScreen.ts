import { IMenuItem } from '../../common/imenuitem';
import { StatusBarData } from '../../common/screen-interfaces/statusBarData';
import { IWorkStation } from '../../common/iworkstation';
import { SelfCheckoutStatusBarData } from '../../common/screen-interfaces/selfCheckoutStatusBarData';
import { ScanSomethingData } from '../../common/controls/scan-something/scanSomthingData';
import { ISellTemplate } from './isell-template';

export interface ISellScreen {
    name: string;
    type: string;
    backButton: IMenuItem;
    logoutButton: IMenuItem;
    template: ISellTemplate;
    sequenceNumber: number;
    locale: string;
    prompt: string;
    workstation: IWorkStation;
    operatorName: string;
    icon: string;
    showScan: boolean;
    showHelp: boolean;
    showSkip: boolean;
    localMenuItems: IMenuItem[];
    theme: string;
    placeholderText: string;
}



export class SellScreenUtils {
    public static getStatusBar(screen: ISellScreen): StatusBarData {
        const statusBar = new StatusBarData();

        statusBar.backButton = screen.backButton;
        statusBar.logoutButton = screen.logoutButton;
        statusBar.operatorName = screen.operatorName;
        statusBar.screenIcon = screen.icon;
        statusBar.screenName = screen.name;
        statusBar.screenType = screen.type;
        if (screen.workstation) {
            statusBar.workstationId = screen.workstation.workstationId;
        }

        return statusBar;
    }

    public static getSelfCheckoutStatusBar(screen: ISellScreen): SelfCheckoutStatusBarData {
        const statusBar = new SelfCheckoutStatusBarData();

        statusBar.backButton = screen.backButton;
        statusBar.showHelp = screen.showHelp;
        statusBar.showScan = screen.showScan;
        statusBar.showSkip = screen.showSkip;
        statusBar.scanPlaceholderText = screen.placeholderText;
        return statusBar;
    }

    public static getScanSomethingData(screen: ISellScreen): ScanSomethingData {
        const scanSomethingData = new ScanSomethingData();
        scanSomethingData.placeholderText = screen.template.scanSomethingText;
        scanSomethingData.autoFocus = screen.template.autoFocusOnScan;

        return scanSomethingData;
    }
}
