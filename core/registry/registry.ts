import { IScreenRegistryItem } from './screen-registry-item.interface';
import { Type } from '@angular/core';
import { IRegistryItem } from './registry-item.interface';
import { IDialogRegistryItem } from './dialog-registry-item.interface';

export class Registry {
    public static screens: IScreenRegistryItem[] = [];
    public static dialogs: IDialogRegistryItem[] = [];

    static getComponents<T>( moduleName: string, items: IRegistryItem<T>[] ): Type<T>[] {
        return items.filter(
                function(item) {
                    return item.moduleName === moduleName;
                }).map( function(item) {
                    return item.component;
                } );
    }

    static getItemsForModule<C, T extends IRegistryItem<C>>( moduleName: string, items: T[] ): T[] {
        return items.filter(
                function(item) {
                    return item.moduleName === moduleName;
                }
        );
    }
}
