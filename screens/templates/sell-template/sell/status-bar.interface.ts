import { IMenuItem } from '../../../../core';

export interface IStatusBar {
    backButton: IMenuItem;
    operatorText: String;
    logoutButton: IMenuItem;
    workstationId: String;
    showScan: boolean;
    showAdmin: boolean;
    showSkip: boolean;
    enableHomeAction: boolean;
}
