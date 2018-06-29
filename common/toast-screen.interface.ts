import {IAbstractScreen} from './screen-interfaces/iAbstractScreen';

export interface IToastScreen extends IAbstractScreen {
	message: string;
    toastType: ToastType;
    duration: number;
}

export enum ToastType {
    Success="Success",
    Warn="Warn"
}