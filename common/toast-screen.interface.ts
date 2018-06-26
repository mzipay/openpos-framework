import {IAbstractScreen} from './screen-interfaces/iAbstractScreen';

export interface IToastScreen extends IAbstractScreen {
	message: string;
	toastType: ToastType;
	duration: Number;
	
}

export enum ToastType {
	Success="Success",
	Error="Error"
}