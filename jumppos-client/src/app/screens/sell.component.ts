import {Component} from '@angular/core';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {SessionService} from '../session.service';

@Component({
  selector: 'app-sell',
  templateUrl: './sell.component.html'
})
export class SellComponent {

  constructor(public session: SessionService) {
  }

}
