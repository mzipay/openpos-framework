import { NgModule } from '@angular/core';

import { SaleComponent } from '../screens-with-parts/sale/sale.component';
import { SharedModule } from '../shared/shared.module';
import { HomeComponent } from './home/home.component';
import { ItemListComponent } from './item-list/item-list.component';
import { SaleRetrievalComponent } from './sale-retrieval/sale-retrieval.component';
import { SellItemDetailComponent } from './sell-item-detail/sell-item-detail.component';
import { BlankComponent } from './templates/blank/blank.component';
import { BlankWithBarComponent } from './templates/blank-with-bar/blank-with-bar.component';
import { SellComponent } from './templates/sell-template/sell/sell.component';
import { TenderingComponent } from './tendering/tendering.component';
import { ReturnComponent } from './return/return.component';
import { ItemSearchResultsComponent } from './item-search-results/item-search-results.component';
import { ChooseOptionsDialogComponent } from './choose-options/choose-options-dialog.component';
import { DynamicFormDialogComponent } from './dynamic-form/dynamic-form-dialog.component';
import { LoadingDialogComponent } from './loading-dialog/loading-dialog.component';
import { MultipleDynamicFormDialogComponent } from './multiple-dynamic-form/multiple-dynamic-form-dialog.component';
import { PromptWithOptionsDialogComponent } from './prompt-with-options/prompt-with-options-dialog.component';


const screens = [
        HomeComponent,
        ItemListComponent,
        SaleRetrievalComponent,
        SellItemDetailComponent,
        BlankComponent,
        BlankWithBarComponent,
        SellComponent,
        TenderingComponent,
        ReturnComponent,
        SaleComponent,
        ItemSearchResultsComponent
    ];

const dialogs = [
        ChooseOptionsDialogComponent,
        DynamicFormDialogComponent,
        LoadingDialogComponent,
        MultipleDynamicFormDialogComponent
    ];
/**
 * @ignore
 */
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
        SharedModule
    ],
    exports: [
        ...screens,
        ...dialogs
    ],
    providers: [
    ]
})
export class TemporarilySharedScreens {}
