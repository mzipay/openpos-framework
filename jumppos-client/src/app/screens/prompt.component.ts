import {Component, ViewChild, AfterViewInit, DoCheck} from '@angular/core';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
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
      this.vc.nativeElement.focus();
    }
  }

  ngAfterViewInit(): void {
    this.initialized = true;
  }

  onEnter(value: string) {
    this.session.onAction('Next');
  }

}
