import { Directive }  from '@angular/core';
import { NG_VALIDATORS } from '@angular/forms';
import { OpenPosValidators } from '../validators';

@Directive({
    selector: '[requireAtleastOne]',
    providers: [{provide: NG_VALIDATORS, useExisting: OpenPosValidators.RequireAtleastOne, multi: true}]
})
export class RequireAtleastOneValidatorDirective {}