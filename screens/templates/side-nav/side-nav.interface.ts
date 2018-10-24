import { IAbstractScreenTemplate, IMenuItem } from '../../../core';

export interface ISideNavTemplate extends IAbstractScreenTemplate {
    drawerMenuItems: IMenuItem[];
    drawerTitle: string;
    selectedMenuItemTitle: string;
}
