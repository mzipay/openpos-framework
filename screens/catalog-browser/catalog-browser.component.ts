import { Component, ViewChild } from '@angular/core';
import { ISellItem } from './../../common/isellitem';
import { IScreen } from './../../common/iscreen';

@Component({
    selector: 'app-catalog-browser',
    templateUrl: './catalog-browser.component.html',
    styleUrls: ['./catalog-browser.component.scss']
  })
  export class CatalogBrowserComponent implements IScreen {
    @ViewChild('drawer') drawer;
  
    screen: IScreen;
    items: ISellItem[];
    categories: string[];
    maxItemsPerPage: number;
    totalItems: number;

    constructor() {

    }

    show(screen: any) {
        this.screen = screen;
        this.items = screen.items;
        this.categories = screen.categories;
        this.maxItemsPerPage = screen.maxItemsPerPage;
        this.totalItems = screen.itemTotalCount;
    }

  }