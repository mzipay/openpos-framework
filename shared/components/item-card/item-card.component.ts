import { Component, Input } from '@angular/core';
import { SessionService, IMenuItem, ISellItem } from '../../../core';


@Component({
  selector: 'app-item-card',
  templateUrl: './item-card.component.html',
})
export class ItemCardComponent {

  @Input() item: ISellItem;
  @Input() session: SessionService;
  @Input() isReadOnly = false;

  public doMenuItemAction(menuItem: IMenuItem, payLoad: any) {
    this.session.onAction(menuItem, payLoad );
  }

  public isMenuItemEnabled(m: IMenuItem): boolean {
    let enabled = m.enabled;
    if (m.action.startsWith('<') && this.session.isRunningInBrowser()) {
      enabled = false;
    }
    return enabled;
  }

}
