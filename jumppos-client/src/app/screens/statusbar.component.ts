import { IMenuItem } from './imenuitem';
import { Component, ViewChild, AfterViewInit, DoCheck } from '@angular/core';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-statusbar',
  templateUrl: './statusbar.component.html'
})
export class StatusBarComponent implements AfterViewInit, DoCheck {

  initialized = false;

  public menuItems: IMenuItem[];

  constructor(public session: SessionService) {
    // this.menuItems = session.screen.menuItems;
  }

  ngDoCheck(): void {
    if (this.initialized) {
      //    this.vc.nativeElement.focus();
    }
  }

  ngAfterViewInit(): void {
    console.log('ngAfterViewInit');
    this.initialized = true;
  }

  onEnter(value: string) {
    this.session.onAction('Save');
  }
}
