import { Component } from '@angular/core';
import { PosScreen } from '../pos-screen/pos-screen.component';
import { SaleInterface } from './sale.interface';
import { ISellItem } from '../../core/interfaces/sell-item.interface';
import { Observable } from 'rxjs/internal/Observable';
import { OpenposMediaService, SelectionMode } from '../../core';
import { SelectableItemListComponentConfiguration } from '../../shared/components/selectable-item-list/selectable-item-list.component';
import { MatDialog } from '@angular/material';

@Component({
  selector: 'app-sale',
  templateUrl: './sale.component.html',
  styleUrls: ['./sale.component.scss']
})
export class SaleComponent extends PosScreen<SaleInterface> {

  items: ISellItem[];
  selectedItems: ISellItem[] = new Array<ISellItem>();
  listConfig = new SelectableItemListComponentConfiguration<ISellItem>();
  overFlowListSize: Observable<number>;
  trainingDrawerOpen = false;

  constructor(private mediaService: OpenposMediaService, protected dialog: MatDialog) {
    super();
    this.overFlowListSize = this.mediaService.mediaObservableFromMap(new Map([
        ['xs', 3],
        ['sm', 3],
        ['md', 4],
        ['lg', 5],
        ['xl', 5]
      ]));

  }

  buildScreen() {
    this.selectedItems = this.screen.items.filter(item => this.screen.selectedItemIndexes.find(index => item.index === index) !== undefined);
    this.listConfig = new SelectableItemListComponentConfiguration<ISellItem>();
    this.listConfig.selectionMode = SelectionMode.Multiple;
    this.listConfig.numResultsPerPage = Number.MAX_VALUE;
    this.listConfig.items = this.screen.items;
    this.items = this.screen.items;
    this.dialog.closeAll();
  }

  onEnter(value: string) {
    this.session.onAction('Next', value);
  }

  public onItemListChange(items: ISellItem[]): void {
    this.screen.selectedItemIndexes = items.map(item => item.index);
    this.session.onValueChange('SelectedItemsChanged', this.screen.selectedItemIndexes);
  }

  public onMenuAction(event: any) {
    if (event.menuItem && event.payload) {
      this.onMenuItemClick(event.menuItem, event.payload);
    } else {
      this.onMenuItemClick(event);
    }
  }

}
