import { PageEvent } from '@angular/material';
import { IForm } from './form.interface';
import { IMenuItem } from './menu-item.interface';
import { ISellItem } from './sell-item.interface';

export interface ICatalogBrowserForm {
    form: IForm;
    pageEvent?: PageEvent;
    selectedCategory?: IMenuItem;
    selectedItems?: ISellItem[];
}
