import { IMenuItem } from './imenuitem';
// import {MdButtonModule, MdCheckboxModule} from '@angular/material';
import {Component, ViewChild, AfterViewInit, DoCheck} from '@angular/core';
import {SessionService} from '../session.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements AfterViewInit, DoCheck {

  public menuItems: IMenuItem[];

  constructor(public session: SessionService) {
    this.menuItems = session.screen.menuItems;
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
