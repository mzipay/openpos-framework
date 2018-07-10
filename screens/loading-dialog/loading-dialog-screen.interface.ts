import { IAbstractScreen } from '../../core';

export interface ILoadingDialogScreen extends IAbstractScreen {
    title: string;
	message: string;
	dismissable: boolean;
	dismissAction: string;
}