import { Component, Input } from '@angular/core';
import { ISellItem } from '../../../common/isellitem';
import { SessionService } from '../../../services/session.service';
import { IMenuItem } from '../../../common/imenuitem';


@Component({
  selector: 'app-checkout-list-item',
  templateUrl: './checkout-list-item.component.html',
})
export class CheckoutListItemComponent {

  @Input() item: ISellItem;
  @Input() session: SessionService;
  @Input() isReadOnly = false;

  public doMenuItemAction(menuItem: IMenuItem, payLoad: any) {
    this.session.onAction(menuItem.action, payLoad, menuItem.confirmationMessage);
  }

  public isMenuItemEnabled(m: IMenuItem): boolean {
    let enabled = m.enabled;
    if (m.action.startsWith('<') && this.session.isRunningInBrowser()) {
      enabled = false;
    }
    return enabled;
  }

}
