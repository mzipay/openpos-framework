import { Component, OnInit, HostListener } from '@angular/core';
import { ObservableMedia} from '@angular/flex-layout';
import { IMenuItem } from '../../core';
import { PosScreen } from '../pos-screen/pos-screen.component';
import { Configuration } from '../../configuration/configuration';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: [ './home.component.scss']
})
export class HomeComponent extends PosScreen<any> implements OnInit {

  screen: any;
  public menuItems: IMenuItem[];
  gutterSize = 40;
  gridColumns = 3;

  constructor( public media: ObservableMedia ) {
    super();
  }

  ngOnInit() {
    this.updateGrid();
    this.media.subscribe(() => {
        this.updateGrid();
    });
  }

  private updateGrid(): void {
    const isLarge = (this.media.isActive('xl') || this.media.isActive('lg') || this.media.isActive('md'));
    const isSmall = (this.media.isActive('sm'));
    this.gridColumns = isLarge ? 3 : (isSmall ? 2 : 1);
    this.gutterSize = isLarge ? 20 : 10;
  }

  buildScreen() {
    this.menuItems = this.screen.menuItems;
  }

  onEnter(value: string) {
    this.session.onAction('Save');
  }

  getClass(): String {
    // return 'main-menu-grid-list';
    return 'foo';
  }

  onMenuItemClick(menuItem: IMenuItem) {
      this.session.onAction( menuItem );
  }

  @HostListener('document:keydown', ['$event'])
  public onKeydown(event: KeyboardEvent) {
    // Map F1 -> F12 to local menu buttons
    if (Configuration.enableKeybinds) {
      const regex = /^F(\d+)$/;
      let bound = false;
      if (regex.test(event.key)) {
        for (const menuItem of this.menuItems) {
          if (menuItem.keybind === event.key) {
            bound = true;
            this.session.onAction(menuItem);
          }
        }
      }
      if (bound) {
        event.preventDefault();
      }
    }
  }

  public keybindsEnabled() {
    return Configuration.enableKeybinds;
  }

}
