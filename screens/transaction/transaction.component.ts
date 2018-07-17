import { MatDialog } from '@angular/material';
import { Component, ViewChild, AfterViewInit, OnInit, AfterViewChecked, ElementRef} from '@angular/core';
import { ObservableMedia } from '@angular/flex-layout';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { DeviceService, ISellItem, SelectionMode } from '../../core';
import { SelectableItemListComponentConfiguration } from '../../shared/components/selectable-item-list/selectable-item-list.component';
import { NavListComponent } from '../../shared/components/nav-list/nav-list.component';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.scss']
})
export class TransactionComponent extends PosScreen<any> implements AfterViewInit, AfterViewChecked, OnInit {

  @ViewChild('box') vc;
  @ViewChild('scrollList') private scrollList: ElementRef;
  public size = -1;
  initialized = false;
  listConfig =  new SelectableItemListComponentConfiguration<ISellItem>();
  selectedItems: ISellItem[] = new Array<ISellItem>();
  individualMenuClicked = false;

  public overFlowListSize: Observable<number>;

  public items: ISellItem[];

  constructor(devices: DeviceService, 
    private observableMedia: ObservableMedia, protected dialog: MatDialog) {
        super();
    }

  buildScreen() {
    this.selectedItems = this.screen.items.filter(item => this.screen.selectedItems.find(selectedItem => item.index === selectedItem.index));
    this.listConfig =  new SelectableItemListComponentConfiguration<ISellItem>();
    this.listConfig.selectionMode = SelectionMode.Multiple;
    this.listConfig.numResultsPerPage = Number.MAX_VALUE;
    this.listConfig.items = this.screen.items;
    this.items = this.screen.items;
    this.dialog.closeAll();
  }

  ngOnInit(): void {
    const sizeMap = new Map([
      ['xs', 3],
      ['sm', 3],
      ['md', 4],
      ['lg', 5],
      ['xl', 5]
    ]);

    let startSize = 3;
    sizeMap.forEach((size, mqAlias) => {
      if( this.observableMedia.isActive(mqAlias)){
        startSize = size;
      }
    });
    this.overFlowListSize = this.observableMedia.asObservable().pipe(map(
      change => {
        return sizeMap.get(change.mqAlias);
      }
    ), startWith(startSize));
  }

  ngAfterViewInit(): void {
    this.initialized = true;
  }

  onEnter(value: string) {
    this.session.response = value;
    this.session.onAction('Next');
  }

  openItemDialog(item: ISellItem) {
    this.individualMenuClicked = true;
    this.openItemsDialog([item]);
  }

  openItemsDialog(items: ISellItem[]) {
    let optionItems = [];
    if(items.length > 1) {
      optionItems = this.screen.multiSelectedMenuItems;
    } else {
      optionItems = items[0].menuItems;
    }
    this.session.response = this.getIndexes(items);
    const dialogRef = this.dialog.open(NavListComponent, {
      width: '70%',
      data: {
        optionItems: optionItems,
        disableClose: false,
        autoFocus: false
     }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  public getIndexes(items: ISellItem[]):number[] {
    var indexes = [];
    items.forEach(item => indexes.push(item.index));
    return indexes;
  }

  public onItemListChange(event: ISellItem[]): void {
    if(this.individualMenuClicked){
      this.individualMenuClicked = false;
      this.selectedItems = event;
      return;
    }
    this.selectedItems = event;
    this.session.onAction("SelectedItemsChanged", this.selectedItems);
  }

  ngAfterViewChecked() {
    if (this.items && this.size !== this.items.length) {
      this.scrollToBottom();
      this.size = this.items.length;
    }
  }

  scrollToBottom(): void {
    try {
      this.scrollList.nativeElement.scrollTop = this.scrollList.nativeElement.scrollHeight;
    } catch (err) { }
  }

}
