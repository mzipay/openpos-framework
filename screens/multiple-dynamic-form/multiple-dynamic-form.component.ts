import { Component } from '@angular/core';
import { IScreen } from '../..';
import { IMultipleDynamicFormScreen } from './multiple-dynamic-form-screen.interface';


@Component({
    selector: 'app-multiple-dynamic-form',
    templateUrl: './multiple-dynamic-form.component.html'
})
export class MultipleDynamicFormComponent implements IScreen {
    screen: IMultipleDynamicFormScreen;

    show(screen: any): void {
        this.screen = screen;
    }
}