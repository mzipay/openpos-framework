import { Type } from '@angular/core';

export interface IRegistryItem<T> {
    moduleName: string;
    component: Type<T>;
}
