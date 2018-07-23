import { Component } from '@angular/core';
import { SelectableItemListComponentConfiguration } from '../../shared/components/selectable-item-list/selectable-item-list.component';
import { IMenuItem, SelectionMode } from '../../core';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
  selector: 'app-selection-list',
  templateUrl: './selection-list.component.html',
  styleUrls: ['./selection-list.component.scss']
})
export class SelectionListComponent extends PosScreen<any> {

  listConfig = new SelectableItemListComponentConfiguration<any>();
  index = -1;

  constructor() {
    super();
  }

  buildScreen() {
    this.listConfig = new SelectableItemListComponentConfiguration<any>();
    if (this.screen.multiSelect) {
      this.listConfig.selectionMode = SelectionMode.Multiple;
    } else {
      this.listConfig.selectionMode = SelectionMode.Single;
    }
    this.listConfig.numResultsPerPage = Number.MAX_VALUE;
    this.listConfig.items = this.screen.selectionList;
  }

  public onItemListChange(event: any[]): void {
    //this.selectedItems = event;
    //this.session.onAction("SelectedItemsChanged", this.selectedItems);
  }

  public onItemChange(event: any): void {
    this.index = this.screen.selectionList.indexOf(event);
  }

  public doMenuItemAction(menuItem: IMenuItem) {
    this.session.onAction(menuItem.action, this.index, menuItem.confirmationMessage);
  }

}
