
import { Component } from '@angular/core';
import { PromptComponent } from './prompt.component';
import { DialogComponent } from '../../shared/decorators/dialog.decorator';

@DialogComponent({
    name: 'Prompt',
    moduleName: 'Core'
})
@Component({
  templateUrl: './prompt-dialog.component.html'
})
export class PromptDialogComponent extends PromptComponent {

}
