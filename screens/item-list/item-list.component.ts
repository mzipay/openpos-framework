import { Component, OnInit, ViewChild, ElementRef, AfterViewInit, AfterViewChecked } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ItemClickAction, MenuClickAction, SelectableItemListComponentConfiguration } from '../../shared/';
import { SelectionMode, IItem, IMenuItem  } from '../../core';
import { NavListComponent } from '../../shared/components/nav-list/nav-list.component';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
    selector: 'app-item-list',
    templateUrl: './item-list.component.html'
})
export class ItemListComponent extends PosScreen<any> implements OnInit, AfterViewInit, AfterViewChecked {

    items: IItem[];
    itemActionName: string;
    text: string;
    itemActions: IMenuItem[] = [];
    condensedListDisplay: false;
    selectionMode: string;
    // @ViewChild('productList') productList: ProductListComponent;

    //  ----------------------------------
    //  Copied from transaction.component
    // @ViewChild('box') vc;
    @ViewChild('scrollList') private scrollList: ElementRef;
    public size = -1;
    initialized = false;
    listConfig =  new SelectableItemListComponentConfiguration<IItem>();
    selectedItems: IItem[] = new Array<IItem>();
    individualMenuClicked = false;
    //  ----------------------------------

    private itemDisplayActionPresent = false;
    private addActionPresent = false;
    private nextActionPresent = false;
  

    constructor(protected dialog: MatDialog) {
        super();
    }

    buildScreen() {
        console.log('### Called list-item component buildScreen()...');

        this.items = this.screen.items;
        this.itemActionName = this.screen.itemActionName;
        this.text = this.screen.text;
        this.itemActions = this.screen.itemActions;
        this.condensedListDisplay = this.screen.condensedListDisplay;
        this.selectionMode = this.screen.selectionMode;

        //  Copied from transaction.component buildScreen() method...

        // console.log('### this.items has ' + (this ? this.items.length : 'unknown') + ' item(s)');
     
        this.listConfig = new SelectableItemListComponentConfiguration<IItem>();
        this.listConfig.selectionMode = SelectionMode.Multiple;
        this.listConfig.numResultsPerPage = Number.MAX_VALUE;
        this.listConfig.items = this.screen.items;
        // console.log('### Initialized listConfig attribute');
 
        // console.log('### About to initialize the selectedItems attribute...');
        if (this.screen.selectedItems)  {
            this.selectedItems = this.screen.items.filter(item => this.screen.selectedItems.find(selectedItem => item.index === selectedItem.index));
            // console.log('### set selectedItems attribute, ' + (this.selectedItems ? this.selectedItems.length : 'unknown') + ' item(s) selected');
        }  else  {
            // console.log('### screen.selectedItems attribute is not populated, initializing selectedItems to empty');
            this.selectedItems = new Array<IItem>();
        }
        // console.log('### Set selectedItems attribute, ' + (this.selectedItems ? this.selectedItems.length : 'unknown') + ' item(s) selected');

        this.determineAvailableActions();

        this.dialog.closeAll();
        // console.log('### list-item component buildScreen() completed successfully');
    }

    ngOnInit(): void {
    }

    ngAfterViewInit(): void {
        this.initialized = true;
    }

    ngAfterViewChecked() {
        if (this.items && this.size !== this.items.length) {
            // this.scrollToBottom();
            this.size = this.items.length;
        }
    }
        
    getSelectionModeAsEnum(): SelectionMode {
        if (this.selectionMode) {
            return SelectionMode[this.selectionMode];
        } else {
            return SelectionMode.Single;
        }
    }

    onItemClick(itemInfo: ItemClickAction): void {
        // console.log('### list-item component onItemClick() fired');
        this.session.response = itemInfo.item;
        this.session.onAction(this.itemActionName);
    }

    onItemSelected(itemInfo: ItemClickAction): void {
        // console.log('### list-item component onItemClick() fired');
        if (this.getSelectionModeAsEnum() === SelectionMode.Multiple) {
            this.session.response = this.selectedItems;
            // this.session.response = this.productList.selectedItems;
        }
    }

    public onItemListChange(event: IItem[]): void {
        // console.log('### list-item component onItemListChange() fired');
           
        if (this.individualMenuClicked) {
            // console.log('### individualMenuClicked was set, flipping to false');
            this.individualMenuClicked = false;
        }

        this.selectedItems = event;
    }

    onActionButtonClick(): void {
        // console.log('### list-item component onActionButtonClick() fired');
        this.session.onAction(this.screen.actionButton.action, null);
    }

    onMenuItemClick(itemInfo: MenuClickAction): void {
        // console.log('### list-item component onMenuItemClick() fired');
        this.session.response = itemInfo.item;
        this.session.onAction(itemInfo.menuItem);
    }

    isItemSelected(item: IItem): boolean  {
        switch (this.listConfig.selectionMode) {
        case SelectionMode.Multiple:
            return this.selectedItems.includes(item);
        case SelectionMode.Single:
            return this.selectedItems[0] === item;
        }
    }

    isItemSelectedDisabled(): boolean {
        return this.getSelectionModeAsEnum() !== SelectionMode.Multiple;
    }

    isItemClickDisabled(): boolean {
        return this.itemActionName === null || this.getSelectionModeAsEnum() === SelectionMode.Multiple;
    }

    isAnyActionPresent(): boolean  {
        return (this.addActionPresent || this.itemDisplayActionPresent || this.nextActionPresent);
    }

    isAddActionPresent(): boolean  {
        //  NOTE: On the Non-Merch list screen, the add action is called 'Next'
        return (this.addActionPresent || this.nextActionPresent);
    }

    isItemDisplayActionPresent(): boolean  {
        return this.itemDisplayActionPresent;
    }

    areAnyItemsSelected(): boolean  {
        return (this.selectedItems.length > 0);
    }

    isSingleItemSelected(): boolean  {
        return (this.selectedItems.length === 1);
    }

    areMultipleItemsSelected(): boolean  {
        return (this.selectedItems.length > 1);
    }

    showItemDetailScreen(items: IItem[]) {
        //  Send the Item Display action to the page.
        this.session.onAction('ItemDisplay', items);
    }

    addItemsToSale(items: IItem[])  {
        //  Send the appropriate Add action to the page.
        const message = (items.length === 1 ? 'Add the selected item to the transaction?' : 'Add the ' + items.length + ' selected items to the transaction?')
        this.session.onAction(this.getAddActionName(), items, message);
    }
 
    openItemDialog(item: IItem) {
        // console.log('### User clicked the More Vertical icon for a single item');
        this.individualMenuClicked = true;
        this.openItemsDialog([item]);
      }
       
    openItemsDialog(items: IItem[]): void {
        let optionItems = [];
        this.session.response = items;  // this.getIndexes(items);
        
        if (items.length > 1) {
            //  NOTE: There are currently no available options for multiple selections.
            // optionItems = this.screen.multiSelectedMenuItems;
        } else {
            optionItems = this.itemActions;
        }
    
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
 
    public getIndexes(items: IItem[]): number[] {
        const indexes = [];
        items.forEach(item => indexes.push(item.index));
        return indexes;
    }
       
    scrollToBottom(): void {
        try {
            this.scrollList.nativeElement.scrollTop = this.scrollList.nativeElement.scrollHeight;
        } catch (err) { }
    }

    private getAddActionName(): string  {
        //  The Add action is named differently for different callers.  Return the 
        //  name that matches our scenario.

        if (this.addActionPresent)  {
            //  Sell Item -> Inquiry scenario.
            return 'Add';
        } else if (this.nextActionPresent)  {
            //  Sell Item -> Non-Marchandise scenario.
            return 'Next'
        }

        // If we made it here, there is no Add action for this scenario.
        return undefined;
    }

    private determineAvailableActions(): void  {
        for (const menu of this.itemActions) {
            if (menu.action === 'ItemDisplay')  {
                // console.log('### ItemDisplay action IS present');
                this.itemDisplayActionPresent = true;
            }  else if (menu.action === 'Add')  {
                // console.log('### Add action IS present');
                this.addActionPresent = true;
            }  else if (menu.action === 'Next')  {
                // console.log('### Next action IS present');
                this.nextActionPresent = true;
            }
        }
    }
}
