import {Component, OnChanges, SimpleChanges} from '@angular/core';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {SessionService} from '../session.service';

@Component({
  selector: 'app-prompt',
  templateUrl: './prompt.component.html'
})
export class PromptComponent implements OnChanges {

  constructor(public session: SessionService) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.session.response = null;
  }


}
