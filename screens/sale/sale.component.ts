import { Component, OnInit } from '@angular/core';
import { PosScreen } from '../pos-screen/pos-screen.component';
import { SaleInterface } from './sale.interface';
import { ISellItem } from '../../core/interfaces/sell-item.interface';
import { Observable } from 'rxjs/internal/Observable';
import { OpenposMediaService, SelectionMode } from '../../core';
import { SelectableItemListComponentConfiguration } from '../../shared/components/selectable-item-list/selectable-item-list.component';
import { ITotal } from '../../core/interfaces/total.interface';
import { TotalType } from '../../core/interfaces/total-type.enum';
import { MatDialog } from '@angular/material';

@Component({
  selector: 'app-sale',
  templateUrl: './sale.component.html',
  styleUrls: ['./sale.component.scss']
})
export class SaleComponent extends PosScreen<SaleInterface> implements OnInit {

  items: ISellItem[];
  selectedItems: ISellItem[] = new Array<ISellItem>();
  listConfig = new SelectableItemListComponentConfiguration<ISellItem>();
  overFlowListSize: Observable<number>;
  amountTotals: ITotal[];
  itemTotal: number;
  trainingDrawerOpen = false;
  showDetails: Observable<boolean>;

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
    this.items = this.screen.items;
    this.amountTotals = this.screen.totals ? (<ITotal[]>this.screen.totals).filter(t => t.type === TotalType.Amount) : null;
    const screenItemTotal = this.screen.totals ? (<ITotal[]>this.screen.totals).find(t => t.type === TotalType.Quantity && t.name === 'itemTotal') : null;
    this.itemTotal = screenItemTotal ? Number(screenItemTotal.amount) : this.items.length;
    this.dialog.closeAll();
  }

  ngOnInit(): void {
    const showDetailsnMap = new Map([
        ['xs', false],
        ['sm', false],
        ['md', true],
        ['lg', true],
        ['xl', true]
    ]);
    this.showDetails = this.mediaService.mediaObservableFromMap(showDetailsnMap);
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
