import { IConfirmationDialog } from './confirmation-dialog.interface';

export interface IMenuItem {
    enabled: boolean;
    action: string;
    title: string;
    icon: string;
    confirmationDialog: IConfirmationDialog;
    confirmationMessage: string;
    buttonSize: string;
    fontSize: string;
    keybind: string;
}
