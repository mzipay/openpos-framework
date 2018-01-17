import { IMenuItem } from './imenuitem';

export interface IUrlMenuItem extends IMenuItem {
    url: string;
    targetMode: string;
}
