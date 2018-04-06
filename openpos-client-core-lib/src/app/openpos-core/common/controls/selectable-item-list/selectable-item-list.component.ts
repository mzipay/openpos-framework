import { Component, Input, OnInit, ContentChild, TemplateRef, ElementRef, Output, EventEmitter } from '@angular/core';

@Component({
    selector: 'app-selectable-item-list',
    templateUrl: './selectable-item-list.component.html',
    styleUrls: ['./selectable-item-list.component.scss']
})
export class SelectableItemListComponent<ItemType> {

    @ContentChild(TemplateRef) itemTemplate: TemplateRef<ElementRef>;

    @Input() 
    set items( items: ItemType[] ){
        this._items = items;
        this.updateResultsToShow();
    }
    get items(): ItemType[]{
        return this._items;
    }

    @Input() 
    set numResultsPerPage( n: number){
        this._numResultsPerPage = n;
        this.updateResultsToShow();
    }
    get numResultsPerPage(): number {
        return this._numResultsPerPage;
    }

    @Input() selectedItem: ItemType;
    @Output() selectedItemChange = new EventEmitter<ItemType>();

    numberOfPages: number;
    itemsToShow : ItemType[];
    currentPage : number = 1;
    
    private _items: ItemType[];
    private _numResultsPerPage: number;

    constructor(){
    }

    updateResultsToShow():void{

        if( this._items.length > 0){
            this.numberOfPages = this._items.length/this.numResultsPerPage;
        }
        this.itemsToShow = this._items.slice((this.currentPage-1)*this.numResultsPerPage,this.numResultsPerPage*this.currentPage);
    }

    onNextPage(){
        this.currentPage++;
        this.updateResultsToShow()
    }

    onPrevPage(){
        this.currentPage--;
        this.updateResultsToShow();
    }

    onItemClick( item: ItemType){
        this.selectedItem = item;
        this.selectedItemChange.emit( item );
    }
}
