import { Component, ViewChild } from '@angular/core';
import {  MatPaginator, PageEvent } from '@angular/material';
import { ISellItem, IMenuItem, IForm, IFormElement, ICatalogBrowserForm } from '../../core/interfaces';
// import { SessionService, FormBuilder, ValidatorsService } from '../../core';
import { PosScreen } from '../pos-screen/pos-screen.component';
import { IItemQuantityFormElement } from './iitem-quantity-form-field.interface';
// import { ValidatorFn, FormGroup } from '@angular/forms';

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
//    formGroup: FormGroup;
/*
    constructor(private formBuilder: FormBuilder, private validatorsService: ValidatorsService) {
        super();
    }
*/
    buildScreen() {
        this.items = this.screen.items;
        this.categories = this.screen.categories;
        this.maxItemsPerPage = this.screen.maxItemsPerPage;
        this.totalItems = this.screen.itemTotalCount;
        this.form = this.screen.form;
        this.selectedItemQuantity = <IItemQuantityFormElement> this.form.formElements.find(e => e.id === 'selectedItemQuantity');
        // this.formGroup = this.formBuilder.group(this.form);
    }

    public onItemSelected(item: ISellItem) {
        // TODO: replace with proper form validation.  Addresses issue with empty quantity not handled on server side
        if (!this.selectedItemQuantity.value) {
            this.selectedItemQuantity.value = '0';
        }
        const returnForm: ICatalogBrowserForm = {selectedItems: [item], form: this.form};
        this.session.onAction('ItemSelected', returnForm);
    }

    public onCategorySelected(category: IMenuItem, event?: any) {
        const returnForm: ICatalogBrowserForm = {selectedCategory: category, form: this.form};
        this.session.onAction(category.action, returnForm);
    }

    public onPageEvent(event?: PageEvent) {
        const returnForm: ICatalogBrowserForm = {pageEvent: event, form: this.form};
        this.session.onAction('PageEvent', returnForm);
    }
  }
