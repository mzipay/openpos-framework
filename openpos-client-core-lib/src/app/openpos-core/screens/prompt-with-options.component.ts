import { FormGroup, FormControl, ValidatorFn, Validators } from '@angular/forms';
import { Component, OnInit, ViewChild } from '@angular/core';
import { SessionService } from '../services/session.service';
import { ChooseOptionsComponent } from './choose-options.component';
import { PromptInputComponent } from '../common/controls/prompt-input.component';

import { PhoneUSValidatorDirective } from '../common/validators/phone.directive';
import { OpenPosValidators } from '../common/validators/openpos-validators';

@Component({
  selector: 'app-prompt-with-options',
  templateUrl: './prompt-with-options.component.html'
})
export class PromptWithOptionsComponent extends ChooseOptionsComponent implements OnInit {

  promptFormGroup: FormGroup;

  constructor(public session: SessionService) {
    super(session);
  }

  public ngOnInit(): void {

    let group: any = {};
    let validators: ValidatorFn[] = [];
    validators.push(Validators.required);
    if(this.session.screen.responseType == "phoneUS"){
      validators.push(OpenPosValidators.PhoneUS);
    }
    group["promptInputControl"] = new FormControl(this.session.screen.responseText, validators);

    if(this.session.screen.showComments){
      group["comments"] = new FormControl(this.session.screen.comments, Validators.required);
    }
    this.promptFormGroup = new FormGroup(group);
  }

  onAction( action: string ): void {
    if ( this.promptFormGroup.valid ) {
      if( this.session.screen.showComments ){
        this.session.onAction( action, 
          { 
            response: this.promptFormGroup.value["promptInputControl"],
            comment: this.promptFormGroup.value["comments"]
          });
      } else {
        this.session.onAction( action, this.promptFormGroup.value["promptInputControl"]);
      }
     }
  }
}
