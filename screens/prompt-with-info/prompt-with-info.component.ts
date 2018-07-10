import { Component } from '@angular/core';
import { PromptComponent } from '../prompt/prompt.component';
import { ValidatorsService } from '../../core';

@Component({
  selector: 'app-prompt-with-info',
  templateUrl: './prompt-with-info.component.html'
})
export class PromptWithInfoComponent extends PromptComponent {

  constructor( private validators: ValidatorsService) {
    super( validators);
  }


}
