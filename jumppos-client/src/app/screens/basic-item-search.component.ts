import { IForm } from './form.component';
import { Component, OnInit, DoCheck } from '@angular/core';
import { SessionService } from '../session.service';
import { IScreen } from '../common/iscreen';

@Component({
  selector: 'app-basic-item-search',
  templateUrl: './basic-item-search.component.html'
})
export class BasicItemSearchComponent implements IScreen, OnInit, DoCheck {
  private lastSequenceNum: number;

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
    console.log(`onValueSelected: ${value}, ${categoryName}`)
    value.selected = true;
    this.session.response = value;
    this.session.onAction(`on${categoryName}Selected`);
  }

  onSubmitAction(submitAction: string): void {
    this.session.response = {
      'searchCategories': this.searchCategories,
      'searchCategoryStructure': this.searchCategoryStructure,
      'searchCategoryValues': this.searchCategoryValues,
      'searchFieldForm': this.searchFieldForm
    };
    this.session.onAction(submitAction);
  }

  protected refreshContent(): void {
    // Loop over each category to determine if the values need updated
    // Screen changed, re-init
    for (let catIdx = 0; catIdx < this.session.screen.searchCategoryValues.length; catIdx++) {
      const srcSearchCategoryValues = this.session.screen.searchCategoryValues[catIdx];
      if (srcSearchCategoryValues.attributes.valuesChanged) {
        console.log(`Updating all values for ${srcSearchCategoryValues.attributes.name}`);
        // If the search category values have changed, just update the whole list of values with
        // what's incoming.
        this.searchCategoryValues[catIdx] = srcSearchCategoryValues;
      } else {
        // Otherwise just update the selection status for any selections that have changed within
        // a category
        console.log(`Checking each value for ${srcSearchCategoryValues.attributes.name}...`);
        const targetSearchCategoryValues = this.searchCategoryValues[catIdx];
        for (let valueIdx = 0; valueIdx < targetSearchCategoryValues.values.length; valueIdx++) {
          const targetValue = targetSearchCategoryValues.values[valueIdx];
          console.log(`Checking  ${targetValue.attributes['name']}...`);
          const srcValue = srcSearchCategoryValues.values[valueIdx];
          if (targetValue.selected !== srcValue.selected) {
            console.log(`Updating  ${targetValue.attributes['name']} selected to ${srcValue.selected}`);
            targetValue.selected = srcValue.selected;
          }
        }
      }
    }
}

  ngDoCheck(): void {
    if (this.session.screen.sequenceNumber !== this.lastSequenceNum) {
        this.refreshContent();
        this.lastSequenceNum = this.session.screen.sequenceNumber;
    }
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
