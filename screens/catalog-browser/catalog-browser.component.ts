import { Component, ViewChild } from '@angular/core';
import {  MatPaginator, PageEvent } from '@angular/material';
import { IScreen } from '../../core/components';
import { ISellItem, IMenuItem, IForm, IFormElement, ICatalogBrowserForm } from '../../core/interfaces';
import { SessionService } from '../../core';
import { PosScreen } from '../pos-screen/pos-screen.component';
import { IItemQuantityFormElement } from './iitem-quantity-form-field.interface';

@Component({
    selector: 'app-catalog-browser',
    templateUrl: './catalog-browser.component.html',
    styleUrls: ['./catalog-browser.component.scss']
  })
  export class CatalogBrowserComponent extends PosScreen<any> {
    @ViewChild('drawer') drawer;
    @ViewChild(MatPaginator) paginator;

    items: ISellItem[];
    categories: IMenuItem[];
    maxItemsPerPage: number;
    totalItems: number;
    selectedItemQuantity: IItemQuantityFormElement;
    form: IForm;

    buildScreen() {
        this.items = this.screen.items;
        this.categories = this.screen.categories;
        this.maxItemsPerPage = this.screen.maxItemsPerPage;
        this.totalItems = this.screen.itemTotalCount;
        this.form = this.screen.form;
        this.selectedItemQuantity = <IItemQuantityFormElement> this.form.formElements.find(e => e.id === 'selectedItemQuantity');
    }

    public onItemSelected(item: ISellItem) {
        const returnForm: ICatalogBrowserForm = {selectedItems: [item], form: this.form};
        this.session.response = returnForm;
        this.session.onAction('ItemSelected');
    }

    public onCategorySelected(category: IMenuItem, event?: any) {
        const returnForm: ICatalogBrowserForm = {selectedCategory: category, form: this.form};
        this.session.response = returnForm;
        this.session.onAction(category.action);
    }

    public onPageEvent(event?: PageEvent) {
        const returnForm: ICatalogBrowserForm = {pageEvent: event, form: this.form};
        this.session.response = returnForm;
        this.session.onAction('PageEvent');
    }
  }
