import { FormControl, FormGroup, ValidatorFn, ValidationErrors, AbstractControl } from '@angular/forms';

export class OpenPosValidators {

    static PhoneUS(c: FormControl) {
        const regex = /^[2-9][0-8][0-9][2-9][0-9]{6}$/;

        if (c.value) {
            return regex.test(c.value) ? null : {
                'phoneUS': {
                    valid: false
                }
            };
        } else {
            return null;
        }
    }

    static PhoneCA(c: FormControl) {
        const regex = /^[0-9]{10}$/;

        if (c.value) {
            return regex.test(c.value) ? null : {
                'phone': {
                    valid: false
                }
            };
        } else {
            return null;
        }
    }

    static GiftCode(c: FormControl) {
        return c.value && c.value.length > 2 ? null : {
            'minlength': {
                valid: false
            }
        };
    }

    static RequireAtleastOne(form: FormGroup) {
        for (const name of Object.getOwnPropertyNames(form.value)) {
            const value = form.value[name];
            if (value !== '') {
                return null;
            }
        }
        return {
            'requireAtleastOne': {
                valid: false
            }
        };
    }

    private static DateValidator(c: FormControl, format: string = 'MMDDYYYY') {
        if (c.value && (typeof c.value  === 'string')) {
            const dateParts = c.value.split('/');
            if (dateParts.length !== 3) {
                return {
                    'date': {
                        valid: false
                    }
                };
            } else {
                const month = Number(dateParts[0]);
                const dayOfMonth = Number(dateParts[1]);
                let year = Number(dateParts[2]);
                const strYear = year + '';
                const formatYCount = (format.match(/y/ig) || []).length;
                // Assume current century for 2 digit year
                if (strYear.length === 1 || strYear.length === 2 ) {
                    const curDate = new Date();
                    const curYear = curDate.getFullYear();
                    // Make assumptions about year in same way that Java SimpleDateFormat does
                    const lowerYear = curYear - 80;
                    const upperYear = curYear + 20;
                    const curCentury = curYear - (curYear % 100);
                    let century = curCentury;
                    if (
                        curCentury + year > upperYear ||
                        (curCentury + year === upperYear && month > curDate.getMonth() + 1) ||
                        (curCentury + year === upperYear && month === curDate.getMonth() + 1 && dayOfMonth > curDate.getDate())
                     ) {
                        century = curCentury - 100;
                    }
                    year = century + year;
                }
                const date = new Date(year, month - 1, dayOfMonth);
                // `vs. parsed date '${date.getMonth() + 1}/${date.getDate()}/${date.getFullYear()}'`);
                if (month === date.getMonth() + 1 && dayOfMonth === date.getDate() && year === date.getFullYear()) {
                    return null;
                } else {
                    return {
                        'date': {
                            valid: false
                        }
                    };
                }
            }
        }

    }
    static DateMMDDYY(c: FormControl) {
        return OpenPosValidators.DateValidator(c, 'MMDDYY');
    }
    static DateMMDDYYYY(c: FormControl) {
        return OpenPosValidators.DateValidator(c, 'MMDDYYYY');
    }
    /** Validates if the value of the given control is greater than 0 */
    static GT_0(c: FormControl) {
        let value = c.value;
        if (value) {
            value = value.replace(',', '');
        }
        return Number(value) > 0 ? null : {
            'gt_0': {
                valid: false
            }
        };
    }

    static LESS_THAN( limit: Number ): ValidatorFn {

        return (c: AbstractControl): ValidationErrors | null => {
            let value = c.value;
            if (value) {
                value = value.replace(',', '');
            }
            return Number(value) < limit ? null : {
                'less_than' : {
                    valid: false
                }
            };
        };
    }

    static GREATER_THAN(limit: Number): ValidatorFn {
        return (c: AbstractControl): ValidationErrors | null => {
            let value = c.value;
            if (value) {
                value = value.replace(',', '');
            }
            return Number(value) > limit ? null : {
                'greater_than' : {
                    valid: false
                }
            };
        };
    }
}
