import { Component, ViewChild } from '@angular/core';
import {  MatPaginator, PageEvent } from '@angular/material';
import { IScreen } from '../../core/components';
import { ISellItem, IMenuItem, IForm, IFormElement, ICatalogBrowserForm } from '../../core/interfaces';
import { SessionService } from '../../core';

@Component({
    selector: 'app-catalog-browser',
    templateUrl: './catalog-browser.component.html',
    styleUrls: ['./catalog-browser.component.scss']
  })
  export class CatalogBrowserComponent implements IScreen {
    @ViewChild('drawer') drawer;
    @ViewChild(MatPaginator) paginator;

    screen: IScreen;
    items: ISellItem[];
    categories: IMenuItem[];
    maxItemsPerPage: number;
    totalItems: number;
    selectedItemQuantity: IFormElement;
    form: IForm;

    constructor(private sessionService: SessionService) {

    }

    show(screen: any) {
        this.screen = screen;
        this.items = screen.items;
        this.categories = screen.categories;
        this.maxItemsPerPage = screen.maxItemsPerPage;
        this.totalItems = screen.itemTotalCount;
        this.form = screen.form;
        this.selectedItemQuantity = this.form.formElements.find(e => e.id === 'selectedItemQuantity');
    }

    public onItemSelected(item: ISellItem) {
        const returnForm: ICatalogBrowserForm = {selectedItems: [item], form: this.form};
        this.sessionService.response = returnForm;
        this.sessionService.onAction('ItemSelected');
    }

    public onCategorySelected(category: IMenuItem, event?: any) {
        const returnForm: ICatalogBrowserForm = {selectedCategory: category, form: this.form};
        this.sessionService.response = returnForm;
        this.sessionService.onAction(category.action);
    }

    public onPageEvent(event?: PageEvent) {
        const returnForm: ICatalogBrowserForm = {pageEvent: event, form: this.form};
        this.sessionService.response = returnForm;
        this.sessionService.onAction('PageEvent');
    }
  }
