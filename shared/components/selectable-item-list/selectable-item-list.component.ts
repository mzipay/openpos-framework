import { Component, Input, ContentChild, TemplateRef, ElementRef, Output, EventEmitter } from '@angular/core';
import { SelectionMode } from '../../../core';

export class SelectableItemListComponentConfiguration<ItemType> {
    numResultsPerPage: number;
    items: ItemType[];
    selectionMode: SelectionMode;
}

@Component({
    selector: 'app-selectable-item-list',
    templateUrl: './selectable-item-list.component.html',
    styleUrls: ['./selectable-item-list.component.scss']
})
export class SelectableItemListComponent<ItemType> {

    @ContentChild(TemplateRef) itemTemplate: TemplateRef<ElementRef>;
    
    @Input() defaultSelect: boolean = false;
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

    constructor() {
    }

    updateResultsToShow(): void {
        if (this._config.items.length > 0) {
            this.numberOfPages = this._config.items.length / this._config.numResultsPerPage;
        }
        this.itemsToShow = this._config.items.slice((this.currentPage - 1) *
            this._config.numResultsPerPage, this._config.numResultsPerPage * this.currentPage);
        
        if(this.defaultSelect && this.itemsToShow.length == 1) {
            this.onItemClick(this.itemsToShow[0]) 
        }
    }

    onNextPage() {
        this.currentPage++;
        this.updateResultsToShow()
    }

    onPrevPage() {
        this.currentPage--;
        this.updateResultsToShow();
    }

    onItemClick(item: ItemType) {
        switch (this._config.selectionMode) {
            case SelectionMode.Multiple:
                let i = this.selectedItemList.indexOf(item);
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
                return this.selectedItem == item;
        }
    }


}
