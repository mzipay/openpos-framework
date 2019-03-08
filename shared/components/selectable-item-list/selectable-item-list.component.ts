import { Component, Input, ContentChild, TemplateRef, ElementRef, Output, EventEmitter, HostListener } from '@angular/core';
import { SelectionMode } from '../../../core';
import { KeyPressProvider } from '../../providers/keypress.provider';
import { Subscription } from 'rxjs';
import { Configuration } from '../../../configuration/configuration';

export class SelectableItemListComponentConfiguration<ItemType> {
    numResultsPerPage: number;
    items: ItemType[];
    defaultSelectItemIndex: number;
    selectionMode: SelectionMode;
}

@Component({
    selector: 'app-selectable-item-list',
    templateUrl: './selectable-item-list.component.html',
    styleUrls: ['./selectable-item-list.component.scss']
})
export class SelectableItemListComponent<ItemType> {

    @ContentChild(TemplateRef) itemTemplate: TemplateRef<ElementRef>;

    @Input() defaultSelect = false;
    @Input() keyboardControl = true;
    @Input()
    set configuration(config: SelectableItemListComponentConfiguration<ItemType>) {
        this._config = config;
        this.updateResultsToShow();
    }
    get configuration(): SelectableItemListComponentConfiguration<ItemType> {
        return this._config;
    }

    @Input() selectedItem: ItemType;
    @Output() selectedItemChange = new EventEmitter<ItemType>();

    @Input() selectedItemList: ItemType[] = new Array<ItemType>();
    @Output() selectedItemListChange = new EventEmitter<ItemType[]>();

    numberOfPages: number;
    itemsToShow: ItemType[];
    currentPage = 1;

    private _config: SelectableItemListComponentConfiguration<ItemType>;

    private subscription: Subscription;

    constructor(private keyPresses: KeyPressProvider) {
        
    }

    ngOnInit(): void {
        this.subscription = this.keyPresses.getKeyPresses().subscribe( event => {
            // ignore repeats
            if ( event.repeat || !Configuration.enableKeybinds ) {
                return;
            }
            if ( event.type === 'keydown') {
                this.onKeydown(event);
            } 
        });
    }

    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }    

    updateResultsToShow(): void {
        if (this._config.items.length > 0) {
            this.numberOfPages = this._config.items.length / this._config.numResultsPerPage;
        }
        this.itemsToShow = this._config.items.slice((this.currentPage - 1) *
            this._config.numResultsPerPage, this._config.numResultsPerPage * this.currentPage);

        if (this.defaultSelect && this._config.defaultSelectItemIndex !== null && this._config.defaultSelectItemIndex !== undefined) {
            switch (this._config.selectionMode) {
                case SelectionMode.Single:
                    this.selectedItem = this.itemsToShow[this._config.defaultSelectItemIndex];
                    this.selectedItemChange.emit(this.selectedItem);
                    break;
                case SelectionMode.Multiple:
                    this.selectedItemList.push(this.itemsToShow[this._config.defaultSelectItemIndex]);
                    this.selectedItemListChange.emit(this.selectedItemList);
                    break;
            }
        } else if (this.defaultSelect && this.itemsToShow.length === 1) {
            switch (this._config.selectionMode) {
                case SelectionMode.Single:
                    this.selectedItem = this.itemsToShow[0];
                    this.selectedItemChange.emit(this.selectedItem);
                    break;
                case SelectionMode.Multiple:
                    this.selectedItemList.push(this.itemsToShow[0]);
                    this.selectedItemListChange.emit(this.selectedItemList);
                    break;
            }
        }
    }

    onNextPage() {
        this.currentPage++;
        this.updateResultsToShow();
    }

    onPrevPage() {
        this.currentPage--;
        this.updateResultsToShow();
    }

    onItemClick(item: ItemType, event: any) {
        // look for block-selection attribute and don't do the selection if we find it in our path
        if ( event.path.find(element => element.attributes && element.attributes.getNamedItem('block-selection'))) {
            return;
        }
        switch (this._config.selectionMode) {
            case SelectionMode.Multiple:
                const i = this.selectedItemList.indexOf(item);
                if (i >= 0) {
                    this.selectedItemList.splice(i, 1);
                } else {
                    this.selectedItemList.push(item);
                }
                this.selectedItemListChange.emit(this.selectedItemList);
                break;
            case SelectionMode.Single:
                this.selectedItem = item;
                this.selectedItemChange.emit(item);
                break;
        }
    }

    isItemSelected(item: ItemType): boolean {
        switch (this._config.selectionMode) {
            case SelectionMode.Multiple:
                return this.selectedItemList.includes(item);
            case SelectionMode.Single:
                return this.selectedItem === item;
        }
    }

    handleEscape(event: KeyboardEvent) {
        switch (this._config.selectionMode) {
            case SelectionMode.Single:
                if (this.selectedItem != null) {
                    this.selectedItem = null;
                    this.selectedItemChange.emit(this.selectedItem);
                    event.preventDefault();
                }
                break;
            case SelectionMode.Multiple:
                if (this.selectedItemList.length > 0) {
                    this.selectedItemList.length = 0;
                    this.selectedItemListChange.emit(this.selectedItemList);
                    event.preventDefault();
                }
                break;
        }    
    }

    handleArrowKey(event: KeyboardEvent) {
        let bound = false;
  
        var direction:number = 1;
        if (event.key === 'ArrowDown') {
          direction = 1;
        } else if (event.key === 'ArrowUp') {
          direction = -1;
        } else {
          return;
        }
  
        // debugger;
    
        var itemIndexToSelect:number = -1;

        switch (this._config.selectionMode) {
            case SelectionMode.Single:
                let currentListIndex = this.itemsToShow.findIndex(item => item === this.selectedItem);
                if (currentListIndex == -1  && direction === -1) {
                    return; // only allow key down to start selecting on the list.
                }
                let newIndex = currentListIndex+direction;
                if (this.itemsToShow.length > newIndex) {
                    this.selectedItem = this.itemsToShow[newIndex];
                    this.selectedItemChange.emit(this.selectedItem);
                } else if (this.selectedItem != null) {
                    this.selectedItem = null;
                    this.selectedItemChange.emit(this.selectedItem);
                }
                
                break;
            case SelectionMode.Multiple:
                if (this.selectedItemList && this.selectedItemList.length === 1) {
                    let currentListIndex = this.itemsToShow.findIndex(item => item === this.selectedItemList[0]);
                    let newIndex = currentListIndex+direction;
                    if (this.itemsToShow.length > newIndex && newIndex > -1) {
                        this.selectedItemList = [this.itemsToShow[newIndex]];
                        this.selectedItemListChange.emit(this.selectedItemList);
                    } else if (this.selectedItemList.length > 0) {
                        this.selectedItemList.length = 0;
                        this.selectedItemListChange.emit(this.selectedItemList);
                    }
                } else if (this.itemsToShow.length > 0) { // only allow key down to start selecting on the list.
                    let index = 0;
                    if (direction === -1) {
                        index = this.itemsToShow.length-1;
                    }
                    this.selectedItemList = [this.itemsToShow[index]];
                    this.selectedItemListChange.emit(this.selectedItemList);
                }
                break;
        }    
    
        if (itemIndexToSelect > -1) {
          bound = true;
          this.selectedItem = this.itemsToShow[itemIndexToSelect];
          this.selectedItemList = [this.itemsToShow[itemIndexToSelect]];
        }
  
        if (bound) {
          event.preventDefault();
        }
    }

 //   @HostListener('document:keyup', ['$event'])
    public onKeydown(event: KeyboardEvent) {
        if (!this.keyboardControl) {
            return;
        }        
        let bound = false;

        var direction:number = 1;
        if (event.key === 'ArrowDown'
        || event.key === 'ArrowUp' ) {
            this.handleArrowKey(event);
        // }  else if (event.key === 'Escape') {
        //     this.handleEscape(event);
        // } else {
          return;
        }
    }      
}
