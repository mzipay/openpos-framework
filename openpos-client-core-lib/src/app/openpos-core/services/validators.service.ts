import { LocaleService } from './locale.service';
import { Injectable } from '@angular/core';
import { ValidatorFn, Validators } from '@angular/forms';
import { OpenPosValidators } from '../common/validators/openpos-validators';

@Injectable()
export class ValidatorsService {

    private validators = new Map<string, Map<string, ValidatorFn>>();

    constructor( private localeService: LocaleService) {
        let USValidators = new Map<string, ValidatorFn>();
        let NOLOCALEValidators = new Map<string, ValidatorFn>();

        USValidators.set('phone', OpenPosValidators.PhoneUS);
        
        NOLOCALEValidators.set('giftcode', OpenPosValidators.GiftCode);

        this.validators.set('en-US', USValidators);
        this.validators.set('NO-LOCALE', NOLOCALEValidators);
    }

    getValidator( name: string ): ValidatorFn{
        let locale = this.localeService.getLocale();
        if( name ){

            let lname = name.toLowerCase();
            // see if we have a validator map for the current locale
            //  and that locale has the validator we need
            if( this.validators.get(locale) && this.validators.get(locale).get(lname)){
                return this.validators.get(locale).get(lname);
            }

            if( this.validators.get('NO-LOCALE') && this.validators.get('NO-LOCALE').get(lname)){
                return this.validators.get('NO-LOCALE').get(lname);
            }
        }
        console.log( `No validator found for locale ${locale} validator name ${name}. Using and always valid validator`);
        return () => { return null };
    }

 
}
