import { PageEvent } from "@angular/material";
import { IForm, ISellItem, IMenuItem } from ".";

export interface ICatalogBrowserForm {
    form: IForm;
    pageEvent?: PageEvent;
    selectedCategory?: IMenuItem;
    selectedItems?: ISellItem[];
}