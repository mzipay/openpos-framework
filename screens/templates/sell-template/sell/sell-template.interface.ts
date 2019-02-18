import { IWorkStation } from './workstation.interface';
import { IActionItem, IAbstractScreenTemplate } from './../../../../core';
import { IScan } from './scan.interface';
import { IStatusBar } from './status-bar.interface';

export interface ISellTemplate extends IAbstractScreenTemplate {
    scan: IScan;
    statusBar: IStatusBar;
    localMenuItems: IActionItem[];
    transactionMenuItems: IActionItem[];
    workstation: IWorkStation;
    operatorText: string;
    timestampBegin: number;
}

