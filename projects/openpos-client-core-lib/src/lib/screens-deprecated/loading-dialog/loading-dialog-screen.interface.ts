import { IAbstractScreen } from '../../core/interfaces/abstract-screen.interface';

export interface ILoadingDialogScreen extends IAbstractScreen {
    title: string;
    message: string;
    dismissable: boolean;
    dismissAction: string;
}
