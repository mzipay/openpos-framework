import { Component } from '@angular/core';
import { IActionItem, SelectionMode } from '../../core';
import { PosScreen } from '../pos-screen/pos-screen.component';

@Component({
  selector: 'app-item-options',
  templateUrl: './item-options.component.html',
  styleUrls: ['./item-options.component.scss']
})
export class ItemOptionsComponent extends PosScreen<any> {

  selectedImage: String;

  constructor() {
    super();
  }

  buildScreen() {
    if (this.screen.imageUrls) {
      this.selectedImage = this.screen.imageUrls[0];
    }
  }

  public doMenuItemAction(menuItem: IActionItem) {
    this.session.onAction(menuItem);
  }

  public selectImage(imageUrl: String) {
    this.selectedImage = imageUrl;
  }

}
