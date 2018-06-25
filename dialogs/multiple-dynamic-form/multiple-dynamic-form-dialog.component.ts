import { Component, OnInit, DoCheck, OnDestroy, ViewChild, ViewChildren, QueryList } from '@angular/core';
import { IScreen, AbstractTemplate } from '../..';
import { MultipleDynamicFormComponent } from '../../screens/multiple-dynamic-form/multiple-dynamic-form.component';
import { DynamicFormControlComponent } from '../../common/controls/dynamic-form-control/dynamic-form-control.component';
import { MatTabChangeEvent, MatTabGroup } from '@angular/material';

@Component({
    selector: 'app-multiple-dynamic-form-dialog',
    templateUrl: './multiple-dynamic-form-dialog.component.html'
})
export class MultipleDynamicFormDialogComponent extends MultipleDynamicFormComponent {
    current:number = 0;
}
