import { Registry } from '../../core/registry';
import { IScreen } from '../../core';
import { Type } from '@angular/core';

export interface DialogProps {
    name: string;
    moduleName: string;
}

export function DialogComponent( config: DialogProps ) {
    return function(target: Type<IScreen>) {
        Registry.dialogs.push( { name: config.name, component: target, moduleName: config.moduleName});
    };
}
