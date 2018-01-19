import { Component, OnInit, ViewChild } from '@angular/core';
import { SessionService } from '../services/session.service';
import { ChooseOptionsComponent } from './choose-options.component';
import { PromptInputComponent } from '../common/controls/prompt-input.component';
@Component({
  selector: 'app-prompt-with-options',
  templateUrl: './prompt-with-options.component.html'
})
export class PromptWithOptionsComponent extends ChooseOptionsComponent implements OnInit {
  @ViewChild(PromptInputComponent)  promptInput: PromptInputComponent;

  constructor(public session: SessionService) {
    super(session);
  }

  public ngOnInit(): void {
  }

  onAction( action: string ): void {
    if ( this.promptInput.responseText ) {
      this.session.onAction( action, this.promptInput.responseText);
    }
  }
}
