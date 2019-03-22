import { IStatusBarControl, IActionItem } from './../../../core/interfaces';
import { Component, forwardRef, Inject } from '@angular/core';
import { StatusBarData } from '../status-bar/status-bar-data';
import { SessionService } from '../../../core/services';

/**
 * A swappable component that is placed on the right side of the status bar.
 * OpenPOS default is to show a logout button there, if it is present on the screen.
 */
@Component({
    selector: 'app-statusbar-status-control',
    templateUrl: './status-bar-status-control.component.html'
})
export class StatusBarStatusControlComponent implements IStatusBarControl {
    data: StatusBarData;

    constructor(@Inject(forwardRef(() => SessionService)) private session: SessionService) {
    }

    public doMenuItemAction(menuItem: IActionItem) {
        this.session.onAction(menuItem);
    }

    public isMenuItemEnabled(m: IActionItem): boolean {
        let enabled = m.enabled;
        if (m.action.startsWith('<') && this.session.isRunningInBrowser()) {
            enabled = false;
        }
        return enabled;
    }
}
