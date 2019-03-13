import { Registry } from '../../core/registry';
import { IScreen } from '../../core';
import { Type } from '@angular/core';

export interface ScreenProps {
    name: string;
    moduleName: string;
}

export function ScreenComponent( config: ScreenProps ) {
    return function(target: Type<IScreen>) {
        Registry.screens.push( { name: config.name, component: target, moduleName: config.moduleName});
    };
}
