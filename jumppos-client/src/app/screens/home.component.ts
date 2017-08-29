import { IScreen } from '../common/iscreen';
import { IMenuItem } from '../common/imenuitem';
// import {MdButtonModule, MdCheckboxModule} from '@angular/material';
import { Component, ViewChild, AfterViewInit, DoCheck } from '@angular/core';
import { SessionService } from '../session.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements AfterViewInit, DoCheck, IScreen {

  public menuItems: IMenuItem[];

  constructor(public session: SessionService) {
    this.menuItems = session.screen.menuItems;
  }

  show(session: SessionService) {
  }

  ngDoCheck(): void {
  }

  ngAfterViewInit(): void {
    console.log('Home Screen');
  }

  onEnter(value: string) {
    this.session.onAction('Save');
  }
}
