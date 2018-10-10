import { IWorkStation } from './workstation.interface';
import { IMenuItem, IAbstractScreenTemplate } from './../../../../core';
import { IScan } from './scan.interface';
import { IStatusBar } from './status-bar.interface';

export interface ISellTemplate extends IAbstractScreenTemplate {
    scan: IScan;
    statusBar: IStatusBar;
    localMenuItems: IMenuItem[];
    transactionMenuItems: IMenuItem[];
    workstation: IWorkStation;
    operatorText: string;
    timestampBegin: number;
}

