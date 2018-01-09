import { IScreen } from '../common/iscreen';
import { Component, ViewChild, AfterViewInit, DoCheck, OnInit } from '@angular/core';
import {SessionService} from '../services/session.service';
import { AbstractApp } from '../common/abstract-app';

@Component({
  selector: 'app-prompt',
  templateUrl: './prompt.component.html'
})
export class PromptComponent implements OnInit, AfterViewInit, DoCheck, IScreen {
  initialized = false;

  constructor(public session: SessionService) {
  }

  show(session: SessionService, app: AbstractApp) {
  }

  public ngOnInit(): void {
  }

  ngDoCheck(): void {
    // if (this.initialized && this.vc && this.vc.nativeElement) {
    //   setTimeout(this.vc.nativeElement.focus(), 0);
    // }
  }

  ngAfterViewInit(): void {
    this.initialized = true;
  }

  onPromptInputEnter(responseText: String): void {
    if (responseText) {
      this.session.response = responseText;
      this.session.onAction(this.session.screen.action);
    }
  }

}
