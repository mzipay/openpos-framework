import { IForm } from './form.component';
import { Component, OnInit, DoCheck, HostListener, OnDestroy } from '@angular/core';
import { SessionService } from '../services/session.service';
import { IScreen } from '../common/iscreen';
import { AbstractApp } from '../common/abstract-app';

@Component({
  selector: 'app-basic-item-search',
  templateUrl: './basic-item-search.component.html'
})
export class BasicItemSearchComponent implements IScreen, OnInit, DoCheck, OnDestroy {

  private lastSequenceNum: number;
  private lastScreenName: string;

  searchCategories: ISearchCategory[];
  searchCategoryStructure: SearchCategoryStructure;
  searchCategoryValues: ISearchCategoryValue[];
  searchFieldForm: IForm;
  searchCategoriesText: string;
  public displayCategoryIndex = 0;

  constructor(public session: SessionService) {
  }

  show(screen: any, app: AbstractApp) {
    this.session.registerActionPayload(this.session.screen.localMenuItems[0].action, () => {
      return this.getSearchPayload();
    });
  }

  ngOnDestroy(): void {
    this.session.unregisterActionPayloads();
  }

  ngOnInit(): void {
    this.searchCategories = this.session.screen.searchCategories;
    this.searchCategoryStructure = this.session.screen.searchCategoryStructure;
    this.searchCategoryValues = this.session.screen.searchCategoryValues;
    this.searchFieldForm = this.session.screen.searchFieldForm;
    this.searchCategoriesText = this.session.screen.searchCategoriesText;
  }

  onValueSelected(value: ISearchCategoryValue, categoryName: string): void {
    value.selected = true;
    this.session.response = {
      'selectedCategoryValue': value,
      'searchFieldForm': this.searchFieldForm
    };
    this.session.onAction(`on${categoryName}Selected`);
  }

  getSearchPayload() {
    return {
      'searchCategories': this.searchCategories,
      'searchCategoryStructure': this.searchCategoryStructure,
      'searchCategoryValues': this.searchCategoryValues,
      'searchFieldForm': this.searchFieldForm
    };
  }

  onSubmitAction(submitAction: string): void {
    // Collect the field values
    this.session.response = this.getSearchPayload();
    this.session.onAction(submitAction);
  }

  protected refreshContent(): void {
    // Loop over each category to determine if the values need updated
    // Screen changed, re-init
    for (let catIdx = 0; catIdx < this.session.screen.searchCategoryValues.length; catIdx++) {
      const srcSearchCategoryValues = this.session.screen.searchCategoryValues[catIdx];
      for (let valueIdx = 0; valueIdx < srcSearchCategoryValues.values.length; valueIdx++) {
        const targetValue = srcSearchCategoryValues.values[valueIdx];
        if (targetValue.selected && targetValue.attributes['name'] !== '<All>') {
          this.displayCategoryIndex = catIdx + 1;
          console.log('setting display to ' + (catIdx + 1) + ' because ' + targetValue.attributes['name'] + ' was selected');
        }
      }
    }
  }

  ngDoCheck(): void {
    if (this.session.screen.type === 'BasicItemSearch'
      && this.session.screen.sequenceNumber !== this.lastSequenceNum) {
      this.refreshContent();
      this.lastSequenceNum = this.session.screen.sequenceNumber;
      this.lastScreenName = this.session.screen.name;
    }
  }

}


export interface ISearchCategory {
  attributes: Map<string, any>;
  searchCategoryType: SearchCategoryType;

}

export interface ISearchCategoryValue {
  attributes: Map<string, any>;
  values: ISearchCategoryValue[];
  selected: boolean;
  index: number;
}

export enum SearchCategoryType {
  ROOT,
  SUBCATEGORY
}

export enum SearchCategoryStructure {
  FLAT,
  HIERARCHICAL
}
