import { Component, OnInit, DoCheck, OnDestroy } from '@angular/core';
import { IScreen, AbstractApp, AbstractTemplate } from '../..';
import { IMultipleDynamicFormScreen } from './imultipleDynamicFormScreen';


@Component({
    selector: 'app-multiple-dynamic-form',
    templateUrl: './multiple-dynamic-form.component.html'
})
export class MultipleDynamicFormComponent implements IScreen {
    screen: IMultipleDynamicFormScreen;

    show(screen: any, app: AbstractApp, template?: AbstractTemplate): void {
        this.screen = screen;
    }
}