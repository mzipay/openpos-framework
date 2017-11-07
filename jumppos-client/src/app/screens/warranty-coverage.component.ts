import { IItem } from './../common/iitem';
import { ISellItem } from '../common/isellitem';
import { IScreen } from '../common/iscreen';
import { IMenuItem } from '../common/imenuitem';
import { Component, ViewChild, AfterViewInit, DoCheck, OnInit } from '@angular/core';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-warranty-coverage',
  templateUrl: './warranty-coverage.component.html'
})
export class WarrantyCoverageComponent implements AfterViewInit, DoCheck, IScreen, OnInit {

  text: string;
  warrantyItems: IItem[];
  warrantyCostTotal: string;

  constructor(public session: SessionService) {

  }

  show(session: SessionService) {
  }

  ngDoCheck(): void {
  }

  ngOnInit(): void {
    this.text = this.session.screen.text;
    this.warrantyItems = this.session.screen.warrantyItems;
    this.warrantyCostTotal = this.session.screen.warrantyCostTotal;
  }

  ngAfterViewInit(): void {
  }

  onItemSelected(value: string) {
    // do nothing
    // this.session.onActionWithStringPayload('Next', value);
  }

}
