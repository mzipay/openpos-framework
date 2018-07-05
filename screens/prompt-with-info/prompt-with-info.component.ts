import { Component } from '@angular/core';
import { PromptComponent } from '../prompt/prompt.component';
import { SessionService } from '../../services/session.service';
import { ValidatorsService } from '../../services/validators.service';

@Component({
  selector: 'app-prompt-with-info',
  templateUrl: './prompt-with-info.component.html'
})
export class PromptWithInfoComponent extends PromptComponent {

  constructor(public session: SessionService, private validators: ValidatorsService) {
    super(session, validators);
  }


}
