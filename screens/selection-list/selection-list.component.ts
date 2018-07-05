import { IScreen } from '../../common/iscreen';
import { Component } from '@angular/core';
import { SessionService } from '../../services/session.service';
import { SelectableItemListComponentConfiguration, SelectionMode } from '../../shared/components/selectable-item-list/selectable-item-list.component';
import { IMenuItem } from '../../common/imenuitem';

@Component({
  selector: 'app-selection-list',
  templateUrl: './selection-list.component.html',
  styleUrls: ['./selection-list.component.scss']
})
export class SelectionListComponent implements IScreen {

  screen: any;
  listConfig = new SelectableItemListComponentConfiguration<any>();
  index: number = -1;

  constructor(public session: SessionService) {
  }

  show(screen: any) {
    this.screen = screen;
    this.listConfig = new SelectableItemListComponentConfiguration<any>();
    if (screen.multiSelect) {
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
