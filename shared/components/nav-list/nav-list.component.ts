import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { IMenuItem } from '../../../common/imenuitem';
import { SessionService } from '../../../services/session.service';

@Component({
    selector: 'app-nav-list',
    templateUrl: './nav-list.component.html',
    styleUrls: ['./nav-list.component.scss']
})
export class NavListComponent {

    optionItems: Array<IMenuItem>;

    constructor(@Inject(MAT_DIALOG_DATA) public data: any,
        public dialogRef: MatDialogRef<NavListComponent>,
        protected session: SessionService) {

        this.optionItems = data.optionItems;
    }

    public doMenuItemAction(menuItem: IMenuItem, payLoad: any) {
        this.session.onAction(menuItem.action, payLoad, menuItem.confirmationMessage);
        this.dialogRef.close();
    }

}
