
import { Component } from '@angular/core';
import { PromptComponent } from './prompt.component';
import { DialogComponent } from '../../shared/decorators/dialog-component.decorator';

@DialogComponent({
    name: 'Prompt'
})
@Component({
  templateUrl: './prompt-dialog.component.html'
})
export class PromptDialogComponent extends PromptComponent {

}
