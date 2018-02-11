import { Directive, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { formDirectiveProvider } from '@angular/forms/src/directives/ng_form';

@Directive({
    selector: '[markDirtyOnSubmit]',
    host: { '(submit)': 'onSubmit($event)' },
})
export class MarkDirtyOnSubmit {

    @Input("markDirtyOnSubmit") formGroup: FormGroup;

    constructor() {

    }

    onSubmit($event: Event): void{
        this.formGroup.markAsDirty();
    }
}
