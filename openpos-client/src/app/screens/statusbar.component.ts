import { IMenuItem } from '../common/imenuitem';
import { Component, ViewChild, AfterViewInit, DoCheck } from '@angular/core';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-statusbar',
  templateUrl: './statusbar.component.html'
})
export class StatusBarComponent implements AfterViewInit, DoCheck {

  constructor(public session: SessionService) {
  }

  ngDoCheck(): void {
  }

  ngAfterViewInit(): void {
  }

}
