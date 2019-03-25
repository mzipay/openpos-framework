import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { IconService } from '../core/services';
import { IconConstants } from '../screens-deprecated/icon.constants';

import { SelectionListScreenComponent } from './selection-list/selection-list-screen.component';
import { SelectionListScreenDialogComponent } from './selection-list/selection-list-screen-dialog.component';
import { TemporarilySharedScreens } from '../screens-deprecated/temporarily-shared-screens.module';
import { GenericDialogComponent } from './dialog/generic-dialog.component';
import { PromptScreenComponent } from './prompt/prompt-screen.component';
import { PromptScreenDialogComponent } from './prompt/prompt-screen-dialog.component';

const screens = [
        SelectionListScreenComponent,
        PromptScreenComponent
    ];

const dialogs = [
        SelectionListScreenDialogComponent,
        GenericDialogComponent,
        PromptScreenDialogComponent
    ];

@NgModule({
    entryComponents: [
        ...screens,
        ...dialogs
    ],
    declarations: [
        ...screens,
        ...dialogs
    ],
    imports: [
        SharedModule,
        TemporarilySharedScreens
    ],
    exports: [
    ],
    providers: [
    ]
})
export class ScreensWithPartsModule {
    constructor( iconService: IconService ) {
        IconConstants.icons.forEach((icon) => {
            iconService.addIcon(icon.name, icon.iconDef);
        });
    }
}
