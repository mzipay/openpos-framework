import { Component, OnInit, OnDestroy, Input, Self, Optional, ElementRef, forwardRef } from '@angular/core';
import { FormGroup, FormBuilder, ControlValueAccessor } from '@angular/forms';
import { MatFormFieldControl } from '@angular/material';
import { NgControl } from '@angular/forms';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import { FocusMonitor } from '@angular/cdk/a11y';
import { coerceBooleanProperty } from '@angular/cdk/coercion';

export class Tel {
  constructor(public area: string, public exchange: string, public subscriber: string){}
}

@Component({
  selector: 'phone-input',
  templateUrl: './phone-input.component.html',
  styleUrls: ['./phone-input.component.scss'],
  providers:[
    { provide: MatFormFieldControl, useExisting: PhoneInputComponent }
  ],
  host: {
    '[class.floating]': 'shouldLabelFloat',
    '[id]': 'id',
    '[attr.aria-describedby]': 'describedBy',
  }
})
export class PhoneInputComponent implements MatFormFieldControl<String>, OnDestroy, ControlValueAccessor {
 
  onChange = (value: string) => {};
  onTouched = () => {};

  writeValue(value: string): void {
    if( value )
      this.value = this.formatPhone(value); 
  }

  registerOnChange(fn: (tel: string) => void ): void {
    this.onChange = fn;
  }
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }
  setDisabledState?(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  static nextId = 0;
  private _required = false;
  private _disabled = false;
  private _placeholder: string;
  private describedBy;

  stateChanges = new Subject<void>();

  formattedPhone: string = "";

  id = `app-phone-input-${PhoneInputComponent.nextId++}`;

  focused = false;
  controlType = 'phone-input';
  
  @Input()
  get placeholder() {
    return this._placeholder;
  }
  set placeholder(plh) {
    this._placeholder = plh;
    this.stateChanges.next();
  }

  @Input()
  get required() {
    return this._required;
  }
  set required(req) {
    this._required = coerceBooleanProperty(req);
    this.stateChanges.next();
  }

  @Input()
  get disabled() {
    return this._disabled;
  }
  set disabled(dis) {
    this._disabled = coerceBooleanProperty(dis);
    this.stateChanges.next();
  }

  @Input()
  get value(): string {
      return this.formattedPhone;
  }
  set value( tel: string ) {
    this.formattedPhone = tel;
    this.stateChanges.next();
  }

  get empty() {
    return this.formattedPhone.length < 1;
  }

  get shouldLabelFloat() {
    return this.focused || !this.empty;
  }

  get errorState() {
    return this.ngControl.touched && (this.ngControl.invalid || this.unFormatPhone(this.value).length < 10); 
  }

  setDescribedByIds(ids: string[]) {
    this.describedBy = ids.join(' ');
  }

  onContainerClick(event: MouseEvent) {
    if ((event.target as Element).tagName.toLocaleLowerCase() != 'input'){
      this.elRef.nativeElement.querySelector('input').focus();
    }
  }

  onKeyUp(value: string){
    let cleanValue = this.unFormatPhone(value);
    this.value = this.formatPhone(cleanValue);
    this.onChange(cleanValue);
  }

  onKeyPress(event: any){
    const pattern = /[0-9\ ]/;
    let inputChar = String.fromCharCode(event.charCode);

    if(!pattern.test(inputChar)){
      event.preventDefault();
    }
  }

  private formatPhone( value: string ): string {
    if( !value ) return "";

    if( value.length < 3 ){
      return `(${value.slice(0)}`;
    }

    if( value.length < 6 ){
      return `(${value.slice(0,3)}) ${value.slice(3,6)}`;
    }

    return `(${value.slice(0,3)}) ${value.slice(3,6)}-${value.slice(6,10)}`;
  }

  private unFormatPhone( value: string ): string {
    let n = value.replace(/\D/g, "");
    return n;
  }

  constructor( fb: FormBuilder, 
                private fm: FocusMonitor,
                @Optional() @Self() public ngControl: NgControl,
                private elRef: ElementRef)  {

    if (this.ngControl) {
      this.ngControl.valueAccessor = this;
    }

    fm.monitor(elRef.nativeElement, true).subscribe(origin => {
      this.focused = !!origin;
      this.stateChanges.next();
    })
  }

  ngOnDestroy(){
    this.stateChanges.complete();
    this.fm.stopMonitoring(this.elRef.nativeElement);
  }
}
