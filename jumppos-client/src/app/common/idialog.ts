import { IMenuItem } from './imenuitem';

export interface IDialog {
    title: string;
    buttons: IMenuItem[];
    subType: string;
    message: string[];
}
