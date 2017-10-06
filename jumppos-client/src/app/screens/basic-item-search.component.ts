import { Component } from '@angular/core';
import { SessionService } from '../session.service';
import { IScreen } from '../common/iscreen';

@Component({
  selector: 'app-basic-item-search',
  templateUrl: './basic-item-search.component.html'
})
export class BasicItemSearchComponent implements IScreen {

  constructor(public session: SessionService) {
  }
  show(session: SessionService) {
  }

}
