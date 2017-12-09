import { ISellItem } from '../common/isellitem';
import { IScreen } from '../common/iscreen';
import { IMenuItem } from '../common/imenuitem';
import {Component, ViewChild, AfterViewInit, DoCheck, OnInit} from '@angular/core';
import {SessionService} from '../services/session.service';
import { AbstractApp } from './abstract-app';
import { ScanSomethingComponent } from '../common/controls/scan-something/scan-something.component';

@Component({
  selector: 'app-sale-retrieval',
  templateUrl: './sale-retrieval.component.html'
})
export class SaleRetrievalComponent implements AfterViewInit, DoCheck, IScreen, OnInit {

  scanInputCallback: Function;

  constructor(public session: SessionService) {

  }
  public ngOnInit(): void {
    this.scanInputCallback = this.onScanInputEnter.bind(this);
  }

  show(session: SessionService, app: AbstractApp) {
  }

  ngDoCheck(): void {
  }

  ngAfterViewInit(): void {
  }

  selected(value: string) {
    this.session.onActionWithStringPayload('Next', value);
  }

  onScanInputEnter($event, scanInput: ScanSomethingComponent): void {
    if (scanInput.responseText) {
        this.session.response = scanInput.responseText;
        this.session.screen.responseText = null;
        scanInput.responseText = null;
        this.session.onAction('Next');
        $event.target.disabled = true;
    }
  }
}
