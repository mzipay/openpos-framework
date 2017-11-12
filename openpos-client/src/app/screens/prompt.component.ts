import { PromptInputComponent } from '../common/controls/prompt-input.component';
import { IScreen } from '../common/iscreen';
import { Component, ViewChild, AfterViewInit, DoCheck, OnInit } from '@angular/core';
import {SessionService} from '../session.service';

@Component({
  selector: 'app-prompt',
  templateUrl: './prompt.component.html'
})
export class PromptComponent implements OnInit, AfterViewInit, DoCheck, IScreen {

  @ViewChild('box') vc;

  initialized = false;
  promptInputCallback: Function;

  constructor(public session: SessionService) {
  }

  show(session: SessionService) {
  }

  public ngOnInit(): void {
    this.promptInputCallback = this.onPromptInputEnter.bind(this);
  }

  ngDoCheck(): void {
    // if (this.initialized && this.vc && this.vc.nativeElement) {
    //   setTimeout(this.vc.nativeElement.focus(), 0);
    // }
  }

  ngAfterViewInit(): void {
    this.initialized = true;
  }

  onPromptInputEnter($event, promptInput: PromptInputComponent): void {
    if (promptInput.responseText) {
        this.session.response = promptInput.responseText;
        this.session.screen.responseText = null;
        promptInput.responseText = null;
        this.session.onAction(this.session.screen.action);
        $event.target.disabled = true;
    }
  }

}
