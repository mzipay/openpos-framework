import {Component, ViewChild, AfterViewInit, DoCheck} from '@angular/core';
import {SessionService} from '../session.service';

@Component({
  selector: 'app-prompt',
  templateUrl: './prompt.component.html'
})
export class PromptComponent implements AfterViewInit, DoCheck {

  @ViewChild('box') vc;

  initialized = false;

  constructor(public session: SessionService) {
  }

  ngDoCheck(): void {
    if (this.initialized && this.vc && this.vc.nativeElement) {
      setTimeout(this.vc.nativeElement.focus(), 0);
    }
  }

  ngAfterViewInit(): void {
    this.initialized = true;
  }

  onEnter($event) {
    if (this.session.response) {
        this.session.onAction('Next');
        $event.target.disabled = true;
    }
  }

}
