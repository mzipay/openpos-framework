import { IActionItem } from '../../../../core';

export interface IStatusBar {
    backButton: IActionItem;
    operatorText: String;
    logoutButton: IActionItem;
    workstationId: String;
    showScan: boolean;
    showAdmin: boolean;
    showSkip: boolean;
    enableHomeAction: boolean;
}
