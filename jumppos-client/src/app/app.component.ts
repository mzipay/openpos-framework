import {Component, OnInit, OnDestroy} from '@angular/core';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {SessionService} from './session.service';

import {PromptComponent} from './screens/prompt.component';
import {SellComponent} from './screens/sell.component';



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit, OnDestroy {

  constructor(public session: SessionService) {
  }

  ngOnInit(): void {
    this.session.subscribe();
  }

  ngOnDestroy(): void {
    this.session.unsubscribe();
  }

}
