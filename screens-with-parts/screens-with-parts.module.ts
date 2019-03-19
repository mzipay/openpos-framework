import { NgModule, Type } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { ScreenService } from '../core/services/screen.service';
import { Registry } from '../core/registry';
import { DialogService, IconService } from '../core';
import { IconConstants } from '../screens-deprecated/icon.constants';
import { SelectionListScreenComponent } from './selection-list/selection-list-screen.component';
import { SelectionListScreenDialogComponent } from './selection-list/selection-list-screen-dialog.component';

@NgModule({
    entryComponents: [
        Registry.getComponents('Core', Registry.screens),
        Registry.getComponents('Core', Registry.dialogs),
        SelectionListScreenComponent,
        SelectionListScreenDialogComponent
    ],
    declarations: [
        Registry.getComponents('Core', Registry.screens),
        Registry.getComponents('Core', Registry.dialogs),
        SelectionListScreenComponent,
        SelectionListScreenDialogComponent
    ],
    imports: [
        SharedModule
    ],
    exports: [
    ],
    providers: [
    ]
})
export class ScreensWithPartsModule {
    constructor(screenService: ScreenService, dialogService: DialogService, iconService: IconService ) {
        Registry.getItemsForModule('Core', Registry.screens).forEach( screen => {
            screenService.addScreen(screen.name, screen.component);
        });

        Registry.getItemsForModule('Core', Registry.dialogs).forEach( dialog => {
            dialogService.addDialog(dialog.name, dialog.component);
        });

        IconConstants.icons.forEach((icon) => {
            iconService.addIcon(icon.name, icon.iconDef);
        });
    }
}
