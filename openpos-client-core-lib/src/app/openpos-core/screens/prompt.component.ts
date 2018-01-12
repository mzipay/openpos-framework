import { PromptInputComponent } from './../common/controls/prompt-input.component';
import { IScreen } from '../common/iscreen';
import { Component, ViewChild, AfterViewInit, DoCheck, OnInit } from '@angular/core';
import {SessionService} from '../services/session.service';
import { AbstractApp } from '../common/abstract-app';

@Component({
  selector: 'app-prompt',
  templateUrl: './prompt.component.html'
})
export class PromptComponent implements OnInit, AfterViewInit, DoCheck, IScreen {
  @ViewChild('promptInput') promptInput: PromptInputComponent;
  initialized = false;

  constructor(public session: SessionService) {
  }

  show(session: SessionService, app: AbstractApp) {
  }

  public ngOnInit(): void {
  }

  ngDoCheck(): void {
  }

  ngAfterViewInit(): void {
    this.initialized = true;
  }

  onAction(action: string) {
    this.session.response = this.promptInput.responseText;
    this.session.screen.responseText = null;
    this.promptInput.responseText = null;
    this.session.onAction(action);
  }

  onPromptInputEnter(responseText: String): void {
    if (responseText) {
      this.session.response = responseText;
      this.session.onAction(this.session.screen.action);
    }
  }

}
