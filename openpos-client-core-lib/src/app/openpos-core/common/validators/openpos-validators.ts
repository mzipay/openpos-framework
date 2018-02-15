import { FormControl, FormGroup } from '@angular/forms';

export class OpenPosValidators{
    
    static PhoneUS(c: FormControl) {
        let regex = /[2-9][0-8]\d[2-9]{6}/
    
        return regex.test(c.value) ? null : {
            'phoneUS': {
                valid: false
            }
        };
    }

    static RequireAtleastOne(form: FormGroup) {
        for( let name of Object.getOwnPropertyNames(form.value)){
            let value = form.value[name];
            if( value != ""){
              return null;
            }
          }
          return { 
              'requireAtleastOne': {
                valid: false
            }
        };
    }
}