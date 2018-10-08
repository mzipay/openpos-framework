import { IWorkStation } from './workstation.interface';
import { IMenuItem } from './../../../../core';
import { IScan } from './scan.interface';
import { IStatusBar } from './status-bar.interface';

export interface ISellTemplate {
    scan: IScan;
    statusBar: IStatusBar;
    localMenuItems: IMenuItem[];
    transactionMenuItems: IMenuItem[];
    workstation: IWorkStation;
    operatorText: string;
    timestampBegin: number;
    registerStatus: string;
    allowRegisterStatusClickAction: boolean;
}

