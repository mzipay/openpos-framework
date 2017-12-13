import { Component, OnInit, ViewChild } from '@angular/core';
import { OverlayContainer } from '@angular/cdk/overlay';
import { AbstractTemplate } from '../../common/abstract-template';
import { SessionService } from '../../services/session.service';

import { IMenuItem } from '../../common/imenuitem';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { ScanSomethingComponent } from '../../common/controls/scan-something/scan-something.component';

@Component({
  selector: 'app-sell',
  templateUrl: './sell.component.html',
  styleUrls: ['./sell.component.scss']
})
export class SellComponent extends AbstractTemplate implements OnInit {

  scanInputCallback: Function;

  @ViewChild('drawer') drawer;
  public isMobile: boolean;

  constructor( public overlayContainer: OverlayContainer, public session: SessionService, breakpointObserver: BreakpointObserver) {
    super(overlayContainer);
    breakpointObserver.observe([
      Breakpoints.Handset
    ]).subscribe(result => {
        this.isMobile = result.matches;
    });
   }

  public ngOnInit(): void {
    this.scanInputCallback = this.onScanInputEnter.bind(this);
  }

  public doMenuItemAction(menuItem: IMenuItem) {
    this.session.onAction(menuItem.action);
}

public isMenuItemEnabled(m: IMenuItem): boolean {
  let enabled = m.enabled;
  if (m.action.startsWith('<') && this.session.isRunningInBrowser()) {
       enabled = false;
  }
  return enabled;
}
  onScanInputEnter($event, scanInput: ScanSomethingComponent): void {
    if (scanInput.responseText) {
        this.session.response = scanInput.responseText;
        this.session.screen.responseText = null;
        scanInput.responseText = null;
        this.session.onAction('Next');
        if ($event.target && $event.target.disabled) {
            $event.target.disabled = true;
        }
    }
  }
}
