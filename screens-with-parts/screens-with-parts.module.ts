import { NgModule, Type } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { ScreenService } from '../core/services/screen.service';
import { SaleFooterComponent } from './sale/sale-footer/sale-footer.component';
import { SaleItemListComponent } from './sale/sale-item-list/sale-item-list.component';
import { Registry } from '../core/registry';
import { DialogService, IconService } from '../core';
import { IconConstants } from '../screens-deprecated/icon.constants';


const screenParts = [
    SaleFooterComponent,
    SaleItemListComponent
];


@NgModule({
    entryComponents: [
        Registry.getComponents('Core', Registry.screens),
        Registry.getComponents('Core', Registry.dialogs)
    ],
    declarations: [
        Registry.getComponents('Core', Registry.screens),
        Registry.getComponents('Core', Registry.dialogs),
        ...screenParts
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
        })
    }
}
