import {
  Component, ViewChild, AfterViewInit, OnInit, OnDestroy,
  Output, Input, EventEmitter
} from '@angular/core';
import { MatInput, MatDialog } from '@angular/material';
import { FormGroup } from '@angular/forms';
import { OptionEntry, DataSource } from '@oasisdigital/angular-material-search-select';
import { Subscription, Observable, of  } from 'rxjs';
import { map } from 'rxjs/operators';
import { ITextMask, TextMask, DynamicDateFormFieldComponent } from '../../../shared';
import { SessionService, ScreenService, PluginService, IFormElement, Scan, BarcodeScannerPlugin } from '../../../core';
import { PopTartComponent } from '../pop-tart/pop-tart.component';

@Component({
  selector: 'app-dynamic-form-field',
  templateUrl: './dynamic-form-field.component.html',
  styleUrls: ['./dynamic-form-field.component.scss']
})
export class DynamicFormFieldComponent implements OnInit, OnDestroy, AfterViewInit {

  @ViewChild(MatInput) field: MatInput;
  @ViewChild(DynamicDateFormFieldComponent) dateField: DynamicDateFormFieldComponent;

  @Input() formField: IFormElement;

  @Input() formGroup: FormGroup;

  public controlName: string;

  public keyboardLayout = 'en-US';

  valuesSubscription: Subscription;
  private barcodeEventSubscription: Subscription;

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

  public focus(): void {
    if ( this.field ) {
        this.field.focus();
    }
    if ( this.dateField ) {
        this.dateField.focus();
    }
  }

  public isReadOnly(): boolean {
      if (this.field) {
          return this.field.readonly;
      }
      return false;
  }

  constructor(public session: SessionService, public screenService: ScreenService, protected dialog: MatDialog,
    private pluginService: PluginService) { }

  ngOnInit() {
    this.controlName = this.formField.id;

    if (this.formField.inputType === 'ComboBox' || this.formField.inputType === 'SubmitOptionList' ||
      this.formField.inputType === 'ToggleButton' || this.formField.inputType === 'PopTart') {
      this.valuesSubscription = this.screenService.getFieldValues(this.formField.id).subscribe((data) => {
        this.values = data;
        console.log('asynchronously received ' + this.values.length + ' items for ' + this.formField.id);
      });
    }

    if (this.formField.inputType === 'NumericText' ||
      this.formField.inputType === 'Phone' ||
      this.formField.inputType === 'PostalCode') {
      this.keyboardLayout = 'Numeric';
    } else if (this.formField.label === 'Email') {
      this.keyboardLayout = 'Email';
    }

    if (this.formField.inputType === 'AutoComplete') {
      this.updateAutoCompleteDataSource2();
    }

    if (this.formField.scanEnabled) {
        this.pluginService.getPluginWithOptions('barcodeScannerPlugin', true, {waitForCordovaInit: true}).then(plugin => {
            // the onBarcodeScanned will only emit an event when client code passes a scan
            // event to the plugin.  This won't be called for cordova barcodescanner plugin
            // camera-based scan events.  It should only be used for third party scan events
            // which come from other sources such as a scan device
            this.barcodeEventSubscription = (<BarcodeScannerPlugin> plugin).onBarcodeScanned.subscribe({
                next: (scan: Scan) => {
                    console.log(`dynamic-form-field '${this.formField.id}' got scan event: ${scan.value}`);
                    if (this.field.focused) {
                        this.setFieldValue(this.formField.id, scan.value);
                    }
                }
            });
            console.log(`dynamic-form-field '${this.formField.id}' is subscribed for barcode scan events`);

        }).catch( error => console.log(`Failed to get barcodeScannerPlugin.  Reason: ${error}`) );

    }
  }

  ngAfterViewInit(): void {
    if (this.formField.inputType === 'AutoComplete') {
      if (this.formField.value) {
        this.formGroup.get(this.formField.id).setValue(this.formField.value);
      }
    }
  }

  ngOnDestroy(): void {
    if (this.valuesSubscription) {
      this.valuesSubscription.unsubscribe();
    }

    if (this.barcodeEventSubscription) {
        this.barcodeEventSubscription.unsubscribe();
    }
  }

  isNumericField(): boolean {
    return (['NumericText', 'Money', 'Phone', 'PostalCode', 'Percent', 'PercentInt', 'Income', 'Decimal'].indexOf(this.formField.inputType) >= 0
        || this.formField.keyboardPreference === 'Numeric');
  }

  onClick(event, formField: IFormElement) {
    if (formField.select) {
      // setSelectionRange is necessary in order to work correctly in UIWebView on iPad
      event.target.setSelectionRange(0, 9999);
    }
  }

  updateAutoCompleteDataSource2() {
    const fld: IFormElement = this.formField;
    const scrnSvc: ScreenService = this.screenService;
    this.autoCompleteDataSource = {
      displayValue(value: any): Observable<OptionEntry | null> {
        console.log(`being asked to display value for: ${value}`);
        const display = <string>value;
        return of({
          value,
          display,
          details: {}
        });
      },

      search(term: string): Observable<OptionEntry[]> {
        const lowerTerm = term ? term.toLowerCase() : '';
        if (lowerTerm) {
          console.log(`autocomplete searching for '${lowerTerm}' on field '${fld.id}'`);
          return (<Observable<Array<string>>>scrnSvc.getFieldValues(fld.id, lowerTerm))
            .pipe(
              map(searchResults => searchResults.map(v => ({
                value: v,
                display: v,
                details: {}
              })))
            );
        } else {
          return of(<OptionEntry[]>[]);
        }
      }
    };

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

  openPopTart() {
    const dialogRef = this.dialog.open(PopTartComponent, {
      width: '70%',
      data: {
        optionItems: this.values,
        disableClose: false,
        autoFocus: false
     }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('pop tart closed with value of: ' + result);
      this.formGroup.get(this.formField.id).setValue(result);
      this.onFormElementChanged(this.formField);
    });
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

  isDateInput(): boolean {
    return this.formField.inputType && this.formField.inputType.toLowerCase().indexOf('date') >= 0;
  }

  isSpecialCaseInput(): boolean {
    return ['ToggleButton', 'Checkbox', 'AutoComplete'].indexOf(this.formField.inputType) >= 0 ||
        this.isDateInput();
  }

  isScanAllowed(): boolean {
    return this.formField.scanEnabled && ! this.formField.disabled &&
        ['NumericText', 'AlphanumericText'].indexOf(this.formField.inputType) >= 0;
  }

  /**
   * This method is invoked when the user presses the Scan button on the field.
   * For device-based scan events, see the ngOnInit method.
   * @param formField The field associated with the scan request.
   */
  onScan(formField: IFormElement): void {
    this.pluginService.getDevicePlugin('barcodeScannerPlugin').then(plugin =>
        plugin.processRequest(
          {requestId: 'scan', deviceId: 'barcode-scanner', type: null, subType: null, payload: null},
          (scan) => {
            if (scan instanceof Scan && ! scan.cancelled) {
                this.setFieldValue(formField.id, scan.value);
            }
          },
          (error) => {
            console.log('Scanning failed: ' + error);
          }
        )
    ).catch( error => console.log(`Scanning failed: ${error}`));
  }

  private setFieldValue(fieldId: string, value: any) {
    const patchGroup = {};
    patchGroup[fieldId] = value;
    this.formGroup.patchValue(patchGroup);
  }
}

