import { Directive }  from '@angular/core';
import { NG_VALIDATORS } from '@angular/forms';
import { OpenPosValidators } from './openpos-validators';

@Directive({
    selector: '[phoneUS]',
    providers: [{provide: NG_VALIDATORS, useExisting: OpenPosValidators.PhoneUS, multi: true}]
})
export class PhoneUSValidatorDirective {}