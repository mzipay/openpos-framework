import { IMenuItem } from './screens/dialog.component';
import { Component, OnInit, OnDestroy, DoCheck } from '@angular/core';
import { SessionService } from './session.service';
import { PromptComponent } from './screens/prompt.component';
import { SellComponent } from './screens/sell.component';
import { FormComponent } from './screens/form.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit, OnDestroy, DoCheck {

  // TODO these should be conflated or removed perhaps.
  public menuItems: IMenuItem[] = [];
  public menuActions: IMenuItem[] = [];
  public backButton: IMenuItem;
  public isCollapsed = true;

  constructor(public session: SessionService) {
  }

  ngOnInit(): void {
    this.session.subscribe();
  }

  ngDoCheck(): void {
    if (typeof this.session.screen !== 'undefined') {
      this.menuItems = this.session.screen.menuItems;
      this.menuActions = this.session.screen.menuActions;
      this.backButton = this.session.screen.backButton;
      if (!this.menuActions || this.menuActions.length === 0) {
        this.isCollapsed = true;
      }

    }
  }

  ngOnDestroy(): void {
    this.session.unsubscribe();
  }
}

export interface IMenuItem {
  enabled: boolean;
  action: string;
  title: string;
  icon: string;
}
