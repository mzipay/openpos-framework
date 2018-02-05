import { Directive, Input }  from '@angular/core';
import { NG_VALIDATORS, Validator, ValidationErrors, FormGroup } from '@angular/forms';

@Directive({
    selector: '[requireAtleastOne]',
    providers: [{provide: NG_VALIDATORS, useExisting: RequireAtleastOneValidatorDirective, multi: true}]
})
export class RequireAtleastOneValidatorDirective implements Validator {

    @Input("requireAtleastOne") requiresAtleastOne: boolean;

    validate(form: FormGroup): ValidationErrors {
      if(!this.requiresAtleastOne) return null;
  
      /*const message = {
        'requireAtleastOne' : {
          'message': 'Atleast one field is required'
        }
      };
  
      for( let control of Object.values(form.controls)){
        if( control.value != ""){
          return null;
        }
      }
  
      return message;*/

      return null;
    }
}