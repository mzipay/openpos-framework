import { Component, Input, Output, EventEmitter } from '@angular/core';
import { IMenuItem, Logger } from '../../../core';
import { MatDialog } from '@angular/material';
import { KebabMenuComponent } from '../kebab-menu/kebab-menu.component';

@Component({
    selector: 'app-kebab-button',
    templateUrl: './kebab-button.component.html',
    styleUrls: ['./kebab-button.component.scss']
})
export class KebabButtonComponent {

    @Input()
    menuItems: IMenuItem[];

    @Input()
    color?: string;

    @Output()
    menuItemClick = new EventEmitter<IMenuItem>();

    constructor(private dialog: MatDialog, private log: Logger) {
    }

    public openKebabMenu() {
        const dialogRef = this.dialog.open(KebabMenuComponent, {
            data: {
                menuItems: this.menuItems,
                payload: null,
                disableClose: false,
                autoFocus: false
            }
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.menuItemClick.emit(result);
            }
        });
    }
}
