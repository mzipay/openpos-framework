import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { IconConstants } from '../screens-deprecated/icon.constants';

import { SelectionListScreenComponent } from './selection-list/selection-list-screen.component';
import { SelectionListScreenDialogComponent } from './selection-list/selection-list-screen-dialog.component';
import { TemporarilySharedScreens } from '../screens-deprecated/temporarily-shared-screens.module';
import { GenericDialogComponent } from './dialog/generic-dialog.component';
import { IconService } from '../core/services/icon.service';
import { PromptScreenComponent } from './prompt/prompt-screen.component';
import { PromptScreenDialogComponent } from './prompt/prompt-screen-dialog.component';
import { PromptWithOptionsScreenComponent } from './prompt-with-options/prompt-with-options-screen.component';
import { PromptWithOptionsScreenDialogComponent } from './prompt-with-options/prompt-with-options-screen-dialog.component';
import { HomeComponent } from './home/home.component';
import { ReturnComponent } from './return/return.component';

const screens = [
    SelectionListScreenComponent,
    PromptScreenComponent,
    PromptWithOptionsScreenComponent,
    HomeComponent,
    ReturnComponent
];

const dialogs = [
    SelectionListScreenDialogComponent,
    GenericDialogComponent,
    PromptScreenDialogComponent,
    PromptWithOptionsScreenDialogComponent
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
