import { Component,Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SessionService } from './session.service';

import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations';

@Component({
  selector: 'home',
  templateUrl: './home.component.html'
})
export class HomeComponent {
  title = 'home';

  constructor(route: ActivatedRoute, session: SessionService) {
    route.queryParams.subscribe( params => {
      if (params['nodeId']) {
        session.nodeId = params['nodeId'];
      }
    });
    
  }
}
