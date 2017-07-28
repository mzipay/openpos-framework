import {Component, OnInit, OnDestroy, DoCheck} from '@angular/core';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {SessionService} from './session.service';

import {PromptComponent} from './screens/prompt.component';
import {SellComponent} from './screens/sell.component';
import {FormComponent} from './screens/form.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit, OnDestroy, DoCheck {

  // TODO these should be conflated or removed perhaps.
  public menuItems: IMenuItem[];
  public menuActions: IMenuItem[];
  public navActions: IMenuItem[];

  constructor(public session: SessionService) {

  }

//  menuItemsFunction(): IMenuItem[] {
//     return this.session.screen.menuItems;
//  }

  ngOnInit(): void {
    this.session.subscribe();
  }

  ngDoCheck(): void {
    if (typeof this.session.screen !== 'undefined') {
      this.menuItems = this.session.screen.menuItems;
      this.menuActions = this.session.screen.menuActions;
      this.navActions = this.session.screen.navActions;
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
    text: string;
    icon: string;
}
