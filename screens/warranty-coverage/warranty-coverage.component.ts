import { Component, ViewChild } from '@angular/core';
import { MatSelectionList, MatListOption } from '@angular/material';
import { IItem } from '../../core';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
  selector: 'app-warranty-coverage',
  templateUrl: './warranty-coverage.component.html'
})
export class WarrantyCoverageComponent extends PosScreen<any> {

    text: string;
    warrantyItems: IItem[];
    warrantyCostTotal: string;
    @ViewChild(MatSelectionList) warrantyItemsSelectionList: MatSelectionList;

    buildScreen() {
        this.text = this.screen.text;
        this.warrantyItemsSelectionList.selectedOptions.clear();
        this.warrantyItems = this.screen.warrantyItems;
        this.warrantyCostTotal = this.screen.warrantyCostTotal;
    }

    onItemSelected(event: Event) {
        const selectedIndexes: number[] = [];

        // FWIW, I've been unable to get binding directly from the mat-list-option.selected property
        // to the warrantyItem.selected to work properly, so instead I get the select options
        // from the warrantyItemsSelectionList.  Not ideal, but it works.
        if (this.warrantyItemsSelectionList.selectedOptions.hasValue) {
        this.warrantyItemsSelectionList.selectedOptions.selected.forEach(
            (matListOption: MatListOption, index: number, array: MatListOption[]) => {
            selectedIndexes.push(matListOption.value);
            }
        );
        }

        this.session.response = selectedIndexes.sort();
        this.session.onAction('SelectedItems');
    }

    onMenuAction(action: string): void {
        this.session.onAction(action);
    }
}
