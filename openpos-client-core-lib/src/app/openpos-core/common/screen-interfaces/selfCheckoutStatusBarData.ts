import { IMenuItem } from "..";
import { ScanSomethingData } from "../controls/scan-something/scanSomthingData";

export class SelfCheckoutStatusBarData{
    showScan: boolean;
    backButton: IMenuItem;
    showSkip: boolean;
    showHelp: boolean;
    scanSomethingData: ScanSomethingData;
}