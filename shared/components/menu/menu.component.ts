import { Component, Input, EventEmitter, Output } from '@angular/core';
import { IMenuItem } from '../../../core/interfaces/menu-item.interface';

@Component({
    selector: 'app-menu',
    templateUrl: './menu.component.html',
    styleUrls: ['./menu.component.scss'],
})
export class MenuComponent {

    @Input() menuItems: IMenuItem[];
    @Output() menuItemClicked = new EventEmitter<IMenuItem>();
}
