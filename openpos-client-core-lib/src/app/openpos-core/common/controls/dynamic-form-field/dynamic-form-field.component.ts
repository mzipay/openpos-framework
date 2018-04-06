import { Subscription } from 'rxjs/Subscription';
import { ITextMask, TextMask } from './../../textmask';
import { IMenuItem } from '../../imenuitem';
import { IScreen } from '../../iscreen';
import {
  Component, ViewChild, AfterViewInit, DoCheck, OnInit, OnDestroy,
  Output, Input, EventEmitter, Optional, ElementRef
} from '@angular/core';
import { SessionService } from '../../../services/session.service';
import { MatSelectChange } from '@angular/material';
import { FormArray, FormBuilder, FormGroup, Validators, AbstractControl, FormControl, NgForm } from '@angular/forms';
import { IFormElement } from '../../iformfield';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { ScreenService } from '../../../services/screen.service';
import { OptionEntry, DataSource } from '@oasisdigital/angular-material-search-select';

@Component({
  selector: 'app-dynamic-form-field',
  templateUrl: './dynamic-form-field.component.html',
  styleUrls: ['./dynamic-form-field.component.scss']
})
export class DynamicFormFieldComponent implements OnInit, OnDestroy {

  @Input() formField: IFormElement;
  @Input() formGroup: FormGroup;

  public keyboardLayout = 'en-US';

  valuesSubscription: Subscription;

  autoCompleteDataSource: DataSource = {
    displayValue(value: any): Observable<OptionEntry | null> {
      return of(null);
    },

    search(term: string): Observable<OptionEntry[]> {
      return of(null);
    }
  };

  // tslint:disable-next-line:no-output-on-prefix
  @Output() onFieldChanged = new EventEmitter<IFormElement>();

  public values: Array<string> = [];

  constructor(public session: SessionService, public screenService: ScreenService) { }

  ngOnInit() {
    if (this.formField.inputType === 'ComboBox' || this.formField.inputType === 'SubmitOptionList' ||
      this.formField.inputType === 'ToggleButton') {
        this.valuesSubscription = this.screenService.getFieldValues(this.formField.id).subscribe((data) => {
        this.values = data;
        console.log('asynchronously received ' + this.values.length + ' items for ' + this.formField.id);
      });
    }

    if (this.formField.inputType === 'NumericText' ||
      this.formField.inputType === 'Phone' ||
      this.formField.inputType === 'PostalCode') {
      this.keyboardLayout = 'Numeric';
    }

    if (this.formField.inputType === 'AutoComplete') {
      this.updateAutoCompleteDataSource();
    }
  }


  ngOnDestroy(): void {
    if (this.valuesSubscription) {
      this.valuesSubscription.unsubscribe();
    }
  }

  updateAutoCompleteDataSource() {
    this.valuesSubscription = this.screenService.getFieldValues(this.formField.id).subscribe((data) => {
      const values: Array<string> = data;
      console.log('asynchronously received ' + values.length + ' items for ' + this.formField.id);
      this.formGroup.get(this.formField.id).setValue(this.formField.value);
      this.autoCompleteDataSource = {
        displayValue(value: any): Observable<OptionEntry | null> {
          const display = <string>value;
          return of({
            value,
            display,
            details: {}
          });
        },

        search(term: string): Observable<OptionEntry[]> {
          console.log(`autocomplete searching for '${term}'`);
          const lowerTerm = typeof term === 'string' ? term.toLowerCase() : '';
          return of(values
            .filter((c: any) => c.toLowerCase().indexOf(lowerTerm) >= 0)
            .slice(0, 1000).map((v: any) => ({
              value: v,
              display: v,
              details: {}
            })));
        }
      };
    });
  }

  onFormElementChanged(formElement: IFormElement): void {
    this.onFieldChanged.emit(formElement);
  }

  getFormFieldMask(): ITextMask {
    if (this.formField.mask) {
      return TextMask.instance(this.formField.mask);
    } else {
      return TextMask.NO_MASK;
    }
  }

  onSubmitOptionSelected(formElement: IFormElement, valueIndex: number, event: Event) {
    if (formElement.selectedIndexes) {
      formElement.selectedIndexes = [valueIndex];
    }

    // this.session.response = this.screenForm;
    // this.session.onAction(formElement.id);
  }

  getPlaceholderText(formElement: IFormElement) {
    let text = '';
    if (formElement.label) {
      text += formElement.label;
    }
    if (text && formElement.placeholder) {
      text = `${text} - ${formElement.placeholder}`;
    }

    return text;
  }
}

