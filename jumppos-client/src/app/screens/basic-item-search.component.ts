import { IForm } from './form.component';
import { Component, OnInit } from '@angular/core';
import { SessionService } from '../session.service';
import { IScreen } from '../common/iscreen';

@Component({
  selector: 'app-basic-item-search',
  templateUrl: './basic-item-search.component.html'
})
export class BasicItemSearchComponent implements IScreen, OnInit {

  searchCategories: SearchCategory[];
  searchCategoryStructure: SearchCategoryStructure;
  searchCategoryValues: SearchCategoryValue[];
  searchFieldForm: IForm;
  // Name of action specified by server side which should behave like
  // a form submission
  submitActionNames: string[] = [];

  constructor(public session: SessionService) {
  }

  show(session: SessionService) {
  }

  ngOnInit(): void {
    this.searchCategories = this.session.screen.searchCategories;
    this.searchCategoryStructure = this.session.screen.searchCategoryStructure;
    this.searchCategoryValues = this.session.screen.searchCategoryValues;
    this.searchFieldForm = this.session.screen.searchFieldForm;
    this.submitActionNames = this.session.screen.submitActionNames;
  }

  onValueSelected(value: SearchCategoryValue, categoryName: string): void {
    value.selected = true;
    this.session.response = value;
    this.session.onAction(`on${categoryName}Selected`);
  }
}

export interface SearchCategory {
  attributes: Map<string, any>;
  searchCategoryType: SearchCategoryType;

}

export interface SearchCategoryValue {
  attributes: Map<string, any>;
  values: SearchCategoryValue[];
  selected: boolean;
}

export enum SearchCategoryType {
  ROOT,
  SUBCATEGORY
}

export enum SearchCategoryStructure {
  FLAT,
  HIERARCHICAL
}
