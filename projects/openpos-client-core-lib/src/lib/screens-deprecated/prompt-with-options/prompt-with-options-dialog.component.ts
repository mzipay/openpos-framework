import { Component } from '@angular/core';
import { PromptWithOptionsComponent } from '../prompt-with-options/prompt-with-options.component';
import { DialogComponent } from '../../shared/decorators/dialog-component.decorator';

@DialogComponent({
    name: 'PromptWithOptions'
})
@Component({
    templateUrl: './prompt-with-options-dialog.component.html'
})
export class PromptWithOptionsDialogComponent extends PromptWithOptionsComponent {
}
