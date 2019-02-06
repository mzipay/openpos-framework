import { Component, ViewChild, ElementRef, Input, Output, EventEmitter } from '@angular/core';
import { ISellItem } from '../../../core/interfaces/sell-item.interface';
import { IMenuItem } from '../../../core';
import { SelectableItemListComponentConfiguration } from '../selectable-item-list/selectable-item-list.component';

@Component({
  selector: 'app-transaction-item-list',
  templateUrl: './transaction-item-list.component.html',
  styleUrls: ['./transaction-item-list.component.scss']
})
export class TransactionItemListComponent {

  @ViewChild('scrollList') private scrollList: ElementRef;

  @Input() listConfig: SelectableItemListComponentConfiguration<ISellItem>;

  @Input() selectedItems: ISellItem[];

  @Input() selectedItemIndexes: number[];

  @Input() multiSelectedMenuItems: IMenuItem[];

  @Input() transactionMenuItems: IMenuItem[];

  @Input() prompt: string;

  @Input() readOnly: boolean;

  @Output() selectedItemListChange = new EventEmitter<ISellItem[]>();

  @Output() menuAction = new EventEmitter<any>();

  individualMenuClicked = false;
  size = -1;

  public onItemListChange(items: ISellItem[]): void {
    this.selectedItems = items;
    if (this.individualMenuClicked) {
      this.individualMenuClicked = false;
      return;
    }
    this.selectedItemListChange.emit(items);
  }

  public onMenuItemClick(menuItem: IMenuItem, payload?: number[]) {
    if (menuItem.enabled && payload) {
      this.menuAction.emit({ menuItem: menuItem, payload: payload });
    } else if (menuItem.enabled) {
      this.menuAction.emit(menuItem);
    }
  }

  /*ngAfterViewChecked() {
    if (this.items && this.size !== this.items.length) {
      this.scrollToBottom();
      this.size = this.items.length;
    }
  }

  scrollToBottom(): void {
    try {
      this.scrollList.nativeElement.scrollTop = this.scrollList.nativeElement.scrollHeight;
    } catch (err) { }
  }*/

}
