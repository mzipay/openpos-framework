import { FormGroup, FormControl } from '@angular/forms';
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
  promptFormGroup = new FormGroup({
    promptInputControl: new FormControl('promptInputControl')
  });
  submitButtonAction: string;

  constructor(public session: SessionService) {
  }

  show(session: SessionService, app: AbstractApp) {
  }

  public ngOnInit(): void {
    this.submitButtonAction = this.session.screen.actionButton.action;
  }

  ngDoCheck(): void {
  }

  ngAfterViewInit(): void {
    this.initialized = true;
  }

  onAction(action: string) {
    if (action === this.submitButtonAction) {
      this.onPromptInputEnter(this.promptInput.responseText);
    } else {
      this.session.response = this.promptInput.responseText;
      this.session.screen.responseText = null;
      this.promptInput.responseText = null;
      this.session.onAction(action);
    }
  }

  onFormSubmit(): void {
    this.onPromptInputEnter(this.promptInput.responseText);
  }

  onPromptInputEnter(responseText: String): void {
    if (responseText) {
      this.promptInput.allowPromptInputValidation = true;
      if (this.promptFormGroup.valid) {
        this.session.response = responseText;
        this.session.onAction(this.session.screen.action);
      }
    }
  }

}
