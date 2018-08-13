import { IMenuItem, ISellItem } from '../../../../core';
import { StatusBarData } from '../../../../shared';
import { SelfCheckoutStatusBarData } from '../../../../self-checkout/self-checkout-statusbar/self-checkout-status-bar-data';
import { ISellTemplate } from './sell-template.interface';
import { SellStatusSectionData } from '../sell-status-section/sell-status-section.data';

export interface ISellScreen {
    name: string;
    screenType: string;
    backButton: IMenuItem;
    logoutButton: IMenuItem;
    template: ISellTemplate;
    locale: string;
    prompt: string;
    icon: string;
    theme: string;
    placeholderText: string;
    hideCustomer: boolean;
    subtitle: string;
    items: ISellItem[];
    selectedItems: ISellItem[];
    multiSelectedMenuItems: IMenuItem[];
    transaction: any;
}



export class SellScreenUtils {
    public static getStatusBar(screen: ISellScreen): StatusBarData {
        const statusBar = new StatusBarData();

        statusBar.backButton = screen.backButton;
        statusBar.logoutButton = screen.logoutButton;
        statusBar.operatorText = screen.template.operatorText;
        statusBar.screenIcon = screen.icon;
        statusBar.screenName = screen.name;
        statusBar.screenType = screen.screenType;
        if (screen.template.workstation) {
            statusBar.workstationId = screen.template.workstation.workstationId;
            statusBar.storeId = screen.template.workstation.storeId;
        }

        return statusBar;
    }

    public static getStatusSection(template: ISellTemplate): SellStatusSectionData {
        const statusSection = new SellStatusSectionData();
        statusSection.registerStatus = template.registerStatus;
        statusSection.timestampBegin = template.timestampBegin;
        return statusSection;
    }

    public static getSelfCheckoutStatusBar(screen: ISellScreen): SelfCheckoutStatusBarData {
        const statusBar = new SelfCheckoutStatusBarData();

        statusBar.backButton = screen.backButton;
        statusBar.showAdmin = screen.template.statusBar.showAdmin;
        statusBar.showScan = screen.template.statusBar.showScan;
        statusBar.showSkip = screen.template.statusBar.showSkip;
        statusBar.scanSomethingData = screen.template.scan;
        return statusBar;
    }

}
