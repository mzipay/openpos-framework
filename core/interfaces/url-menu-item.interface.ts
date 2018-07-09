import { IMenuItem } from './menu-item.interface';

export interface IUrlMenuItem extends IMenuItem {
    url: string;
    targetMode: string;
    options: string;  // Corresponds to options/features that can be passed to window.open()
}
