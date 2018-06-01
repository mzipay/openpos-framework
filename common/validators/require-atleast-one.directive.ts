import { Directive }  from '@angular/core';
import { NG_VALIDATORS, Validator  } from '@angular/forms';
import { OpenPosValidators } from './openpos-validators';

@Directive({
    selector: '[requireAtleastOne]',
    providers: [{provide: NG_VALIDATORS, useExisting: OpenPosValidators.RequireAtleastOne, multi: true}]
})
export class RequireAtleastOneValidatorDirective {}