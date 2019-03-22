import { Component, Input } from '@angular/core';
import { SessionService, IActionItem, ISellItem } from '../../../core';


@Component({
  selector: 'app-item-card',
  templateUrl: './item-card.component.html',
  styleUrls: ['./item-card.component.scss']
})
export class ItemCardComponent {

  @Input() item: ISellItem;
  @Input() session: SessionService;
  @Input() isReadOnly = false;

  public doMenuItemAction(menuItem: IActionItem, payLoad: any) {
    this.session.onAction(menuItem, payLoad );
  }

  public isMenuItemEnabled(m: IActionItem): boolean {
    let enabled = m.enabled;
    if (m.action.startsWith('<') && this.session.isRunningInBrowser()) {
      enabled = false;
    }
    return enabled;
  }

}
