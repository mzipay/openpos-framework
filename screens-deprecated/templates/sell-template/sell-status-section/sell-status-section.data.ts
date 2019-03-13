import { ISystemStatus } from '../../../../core/interfaces/system-status.interface';

export class SellStatusSectionData {
    storeNumber: string;
    registerNumber: string;
    systemStatus: ISystemStatus;
    timestampBegin: number;
}
